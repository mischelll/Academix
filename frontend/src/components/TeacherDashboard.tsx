import React, { useState, useEffect } from 'react';
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from './ui/card';
import { Button } from './ui/button';
import { Badge } from './ui/badge';
import { Textarea } from './ui/textarea';
import { Input } from './ui/input';
import { Label } from './ui/label';
import { Dialog, DialogContent, DialogDescription, DialogFooter, DialogHeader, DialogTitle, DialogTrigger } from './ui/dialog';
import { Tabs, TabsContent, TabsList, TabsTrigger } from './ui/tabs';
import { Separator } from './ui/separator';
import { toast } from 'sonner';
import { 
  Download, 
  FileText, 
  User, 
  Calendar, 
  Star, 
  MessageSquare,
  CheckCircle,
  Clock,
  AlertCircle,
  Shield
} from 'lucide-react';
import { fetchTeacherHomeworks, gradeHomework, downloadHomework, TeacherHomework, GradeHomeworkRequest } from '../api/homework';
import { useUserStore } from '../stores/userStore';
import { useNavigate } from 'react-router-dom';

const TeacherDashboard: React.FC = () => {
  const [homeworks, setHomeworks] = useState<TeacherHomework[]>([]);
  const [loading, setLoading] = useState(true);
  const [gradingLoading, setGradingLoading] = useState<number | null>(null);
  const [selectedHomework, setSelectedHomework] = useState<TeacherHomework | null>(null);
  const [grade, setGrade] = useState<number>(0);
  const [comment, setComment] = useState('');
  const [isGradingDialogOpen, setIsGradingDialogOpen] = useState(false);
  
  const user = useUserStore((state) => state.user);
  const navigate = useNavigate();

  // Check if user has teacher or admin role
  const isTeacher = user?.roles?.some(role => 
    role.name === 'ROLE_TEACHER' || role.name === 'ROLE_ADMIN'
  );

  // Redirect if not a teacher
  useEffect(() => {
    if (user && !isTeacher) {
      toast.error("Access denied. Teacher privileges required.");
      navigate('/');
    }
  }, [user, isTeacher, navigate]);

  // Show access denied message if not a teacher
  if (user && !isTeacher) {
    return (
      <div className="container mx-auto p-6">
        <Card className="max-w-md mx-auto mt-10">
          <CardHeader className="text-center">
            <div className="mx-auto mb-4 flex h-12 w-12 items-center justify-center rounded-full bg-red-100">
              <Shield className="h-6 w-6 text-red-600" />
            </div>
            <CardTitle className="text-xl">Access Denied</CardTitle>
            <CardDescription>
              You need teacher privileges to access this dashboard.
            </CardDescription>
          </CardHeader>
          <CardContent className="text-center">
            <Button onClick={() => navigate('/')} className="w-full">
              Go Back Home
            </Button>
          </CardContent>
        </Card>
      </div>
    );
  }

  useEffect(() => {
    if (isTeacher) {
      loadHomeworks();
    }
  }, [isTeacher]);

  const loadHomeworks = async () => {
    try {
      setLoading(true);
      const data = await fetchTeacherHomeworks();
      setHomeworks(data);
    } catch (error) {
      console.error('Error loading homeworks:', error);
      toast.error("Failed to load homeworks");
    } finally {
      setLoading(false);
    }
  };

  const handleDownload = async (lessonId: number, studentName: string) => {
    try {
      await downloadHomework(lessonId);
      toast.success(`Downloaded homework for ${studentName}`);
    } catch (error) {
      console.error('Error downloading homework:', error);
      toast.error("Failed to download homework");
    }
  };

  const handleGrade = async () => {
    if (!selectedHomework) return;

    try {
      setGradingLoading(selectedHomework.id);
      const request: GradeHomeworkRequest = {
        homeworkId: selectedHomework.id,
        grade: grade,
        comment: comment
      };

      await gradeHomework(request);
      
      // Update the homework in the list
      setHomeworks(prev => prev.map(hw => 
        hw.id === selectedHomework.id 
          ? { ...hw, grade, comment, status: 'REVIEWED' }
          : hw
      ));

      toast.success(`Homework graded successfully with grade ${grade}`);

      setIsGradingDialogOpen(false);
      setSelectedHomework(null);
      setGrade(0);
      setComment('');
    } catch (error) {
      console.error('Error grading homework:', error);
      toast.error("Failed to grade homework");
    } finally {
      setGradingLoading(null);
    }
  };

  const openGradingDialog = (homework: TeacherHomework) => {
    setSelectedHomework(homework);
    setGrade(homework.grade || 0);
    setComment(homework.comment || '');
    setIsGradingDialogOpen(true);
  };

  const getStatusIcon = (status: string) => {
    switch (status) {
      case 'SUBMITTED':
        return <Clock className="h-4 w-4 text-yellow-500" />;
      case 'REVIEWED':
        return <CheckCircle className="h-4 w-4 text-green-500" />;
      default:
        return <AlertCircle className="h-4 w-4 text-gray-500" />;
    }
  };

  const getStatusBadge = (status: string) => {
    switch (status) {
      case 'SUBMITTED':
        return <Badge variant="secondary">Submitted</Badge>;
      case 'REVIEWED':
        return <Badge variant="default">Reviewed</Badge>;
      default:
        return <Badge variant="outline">{status}</Badge>;
    }
  };

  const submittedHomeworks = homeworks.filter(hw => hw.status === 'SUBMITTED');
  const reviewedHomeworks = homeworks.filter(hw => hw.status === 'REVIEWED');

  if (loading) {
    return (
      <div className="flex items-center justify-center min-h-[400px]">
        <div className="animate-spin rounded-full h-8 w-8 border-b-2 border-primary"></div>
      </div>
    );
  }

  return (
    <div className="container mx-auto p-6 space-y-6">
      <div className="flex items-center justify-between">
        <div>
          <h1 className="text-3xl font-bold">Teacher Dashboard</h1>
          <p className="text-muted-foreground">Grade and manage student homeworks</p>
        </div>
        <Button onClick={loadHomeworks} variant="outline">
          Refresh
        </Button>
      </div>

      <Tabs defaultValue="submitted" className="space-y-4">
        <TabsList>
          <TabsTrigger value="submitted">
            Pending Review ({submittedHomeworks.length})
          </TabsTrigger>
          <TabsTrigger value="reviewed">
            Reviewed ({reviewedHomeworks.length})
          </TabsTrigger>
        </TabsList>

        <TabsContent value="submitted" className="space-y-4">
          {submittedHomeworks.length === 0 ? (
            <Card>
              <CardContent className="flex flex-col items-center justify-center py-12">
                <FileText className="h-12 w-12 text-muted-foreground mb-4" />
                <h3 className="text-lg font-semibold mb-2">No homeworks to review</h3>
                <p className="text-muted-foreground text-center">
                  All submitted homeworks have been reviewed.
                </p>
              </CardContent>
            </Card>
          ) : (
            <div className="grid gap-4 md:grid-cols-2 lg:grid-cols-3">
              {submittedHomeworks.map((homework) => (
                <Card key={homework.id} className="hover:shadow-lg transition-shadow">
                  <CardHeader>
                    <div className="flex items-start justify-between">
                      <div className="flex items-center space-x-2">
                        {getStatusIcon(homework.status)}
                        {getStatusBadge(homework.status)}
                      </div>
                      <Button
                        variant="ghost"
                        size="sm"
                        onClick={() => handleDownload(homework.lessonId, homework.studentName)}
                      >
                        <Download className="h-4 w-4" />
                      </Button>
                    </div>
                    <CardTitle className="text-lg">{homework.title}</CardTitle>
                    <CardDescription className="line-clamp-2">
                      {homework.description}
                    </CardDescription>
                  </CardHeader>
                  <CardContent className="space-y-3">
                    <div className="flex items-center space-x-2 text-sm text-muted-foreground">
                      <User className="h-4 w-4" />
                      <span>{homework.studentName}</span>
                    </div>
                    <div className="flex items-center space-x-2 text-sm text-muted-foreground">
                      <Calendar className="h-4 w-4" />
                      <span>Submitted: {new Date(homework.submittedDate).toLocaleDateString()}</span>
                    </div>
                    <div className="flex items-center space-x-2 text-sm text-muted-foreground">
                      <Calendar className="h-4 w-4" />
                      <span>Deadline: {new Date(homework.deadline).toLocaleDateString()}</span>
                    </div>
                    <Separator />
                    <Button 
                      onClick={() => openGradingDialog(homework)}
                      className="w-full"
                    >
                      <Star className="h-4 w-4 mr-2" />
                      Grade Homework
                    </Button>
                  </CardContent>
                </Card>
              ))}
            </div>
          )}
        </TabsContent>

        <TabsContent value="reviewed" className="space-y-4">
          {reviewedHomeworks.length === 0 ? (
            <Card>
              <CardContent className="flex flex-col items-center justify-center py-12">
                <CheckCircle className="h-12 w-12 text-muted-foreground mb-4" />
                <h3 className="text-lg font-semibold mb-2">No reviewed homeworks</h3>
                <p className="text-muted-foreground text-center">
                  Reviewed homeworks will appear here.
                </p>
              </CardContent>
            </Card>
          ) : (
            <div className="grid gap-4 md:grid-cols-2 lg:grid-cols-3">
              {reviewedHomeworks.map((homework) => (
                <Card key={homework.id} className="hover:shadow-lg transition-shadow">
                  <CardHeader>
                    <div className="flex items-start justify-between">
                      <div className="flex items-center space-x-2">
                        {getStatusIcon(homework.status)}
                        {getStatusBadge(homework.status)}
                      </div>
                      <Button
                        variant="ghost"
                        size="sm"
                        onClick={() => handleDownload(homework.lessonId, homework.studentName)}
                      >
                        <Download className="h-4 w-4" />
                      </Button>
                    </div>
                    <CardTitle className="text-lg">{homework.title}</CardTitle>
                    <CardDescription className="line-clamp-2">
                      {homework.description}
                    </CardDescription>
                  </CardHeader>
                  <CardContent className="space-y-3">
                    <div className="flex items-center space-x-2 text-sm text-muted-foreground">
                      <User className="h-4 w-4" />
                      <span>{homework.studentName}</span>
                    </div>
                    <div className="flex items-center space-x-2 text-sm">
                      <Star className="h-4 w-4 text-yellow-500" />
                      <span className="font-semibold">Grade: {homework.grade || 'N/A'}</span>
                    </div>
                    {homework.comment && (
                      <div className="flex items-start space-x-2 text-sm">
                        <MessageSquare className="h-4 w-4 text-muted-foreground mt-0.5" />
                        <span className="text-muted-foreground line-clamp-2">
                          {homework.comment}
                        </span>
                      </div>
                    )}
                    <Separator />
                    <Button 
                      onClick={() => openGradingDialog(homework)}
                      variant="outline"
                      className="w-full"
                    >
                      <Star className="h-4 w-4 mr-2" />
                      Update Grade
                    </Button>
                  </CardContent>
                </Card>
              ))}
            </div>
          )}
        </TabsContent>
      </Tabs>

      {/* Grading Dialog */}
      <Dialog open={isGradingDialogOpen} onOpenChange={setIsGradingDialogOpen}>
        <DialogContent className="sm:max-w-[500px]">
          <DialogHeader>
            <DialogTitle>Grade Homework</DialogTitle>
            <DialogDescription>
              Grade the homework for {selectedHomework?.studentName}
            </DialogDescription>
          </DialogHeader>
          <div className="space-y-4">
            <div>
              <Label htmlFor="grade">Grade (0-100)</Label>
              <Input
                id="grade"
                type="number"
                min="0"
                max="100"
                value={grade}
                onChange={(e) => setGrade(Number(e.target.value))}
                placeholder="Enter grade"
              />
            </div>
            <div>
              <Label htmlFor="comment">Comment</Label>
              <Textarea
                id="comment"
                value={comment}
                onChange={(e) => setComment(e.target.value)}
                placeholder="Enter feedback for the student"
                rows={4}
              />
            </div>
          </div>
          <DialogFooter>
            <Button
              variant="outline"
              onClick={() => setIsGradingDialogOpen(false)}
            >
              Cancel
            </Button>
            <Button
              onClick={handleGrade}
              disabled={gradingLoading === selectedHomework?.id}
            >
              {gradingLoading === selectedHomework?.id ? (
                <div className="animate-spin rounded-full h-4 w-4 border-b-2 border-white mr-2"></div>
              ) : (
                <Star className="h-4 w-4 mr-2" />
              )}
              Submit Grade
            </Button>
          </DialogFooter>
        </DialogContent>
      </Dialog>
    </div>
  );
};

export default TeacherDashboard; 