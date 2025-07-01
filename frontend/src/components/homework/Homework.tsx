import {
  Table,
  TableHeader,
  TableRow,
  TableHead,
  TableBody,
  TableCell,
  TableFooter,
} from "../ui/table";
import { Button } from "../ui/button";
import { Check, X, Download, Clock, FileText } from "lucide-react";
import { useQuery } from "@tanstack/react-query";
import { fetchUserHomeworks, downloadHomework, HomeworkStatus } from "../../api/homework";
import type { Homework } from "../../api/homework";
import { useUserStore } from "../../stores/userStore";
import { format } from "date-fns";
import { Card, CardHeader, CardTitle, CardDescription, CardContent } from "../ui/card";

function StatusBadge({ status }: { status: HomeworkStatus }) {
  let color = "bg-gray-100 text-gray-700";
  let icon = <FileText className="w-4 h-4 mr-1" />;
  let label = status;
  switch (status) {
    case HomeworkStatus.REVIEWED:
      color = "bg-green-100 text-green-700";
      icon = <Check className="w-4 h-4 mr-1" />;
      label = "Reviewed";
      break;
    case HomeworkStatus.SUBMITTED:
      color = "bg-blue-100 text-blue-700";
      icon = <Clock className="w-4 h-4 mr-1" />;
      label = "Pending Review";
      break;
    case HomeworkStatus.REVIEWING:
      color = "bg-yellow-100 text-yellow-700";
      icon = <Clock className="w-4 h-4 mr-1" />;
      label = "Under Review";
      break;
    case HomeworkStatus.RETURNED:
      color = "bg-red-100 text-red-700";
      icon = <X className="w-4 h-4 mr-1" />;
      label = "Returned";
      break;
    case HomeworkStatus.OPEN:
      color = "bg-gray-100 text-gray-700";
      icon = <FileText className="w-4 h-4 mr-1" />;
      label = "Open";
      break;
  }
  return (
    <span className={`inline-flex items-center rounded px-2 py-1 text-xs font-medium ${color}`}>
      {icon} {label}
    </span>
  );
}

function EmptyState({ text }: { text: string }) {
  return (
    <div className="flex flex-col items-center justify-center py-12 text-gray-400">
      <FileText className="w-10 h-10 mb-2" />
      <div className="text-lg font-medium">{text}</div>
    </div>
  );
}

export default function Homework() {
  const user = useUserStore((state) => state.user);

  const { data: homeworks, isLoading, error } = useQuery({
    queryKey: ["userHomeworks", user?.id],
    queryFn: () => fetchUserHomeworks(user!.id),
    enabled: !!user?.id,
  });

  if (isLoading) {
    return <div className="flex items-center justify-center h-64 text-lg">Loading homeworks...</div>;
  }

  if (error) {
    return <div className="flex items-center justify-center h-64 text-lg text-red-600">Error loading homeworks</div>;
  }

  const homeworkList = homeworks?.content || homeworks || [];

  if (!homeworkList.length) {
    return <EmptyState text="No homeworks found." />;
  }

  // Calculate statistics
  const stats = {
    submitted: homeworkList.filter((h: Homework) => h.status === HomeworkStatus.SUBMITTED).length,
    reviewing: homeworkList.filter((h: Homework) => h.status === HomeworkStatus.REVIEWING).length,
    reviewed: homeworkList.filter((h: Homework) => h.status === HomeworkStatus.REVIEWED).length,
    returned: homeworkList.filter((h: Homework) => h.status === HomeworkStatus.RETURNED).length,
  };

  const handleDownload = async (lessonId: number) => {
    try {
      await downloadHomework(lessonId);
    } catch (error) {
      console.error("Failed to download homework:", error);
    }
  };

  const formatDate = (dateString: string) => {
    try {
      return format(new Date(dateString), "MMM dd, yyyy");
    } catch {
      return "Invalid date";
    }
  };

  return (
    <Card className="w-full max-w-4xl mx-auto mt-10 shadow-lg border">
      <CardHeader>
        <CardTitle className="text-2xl font-bold">My Homework Assignments</CardTitle>
        <CardDescription>Track your homework progress, download files, and see your grades.</CardDescription>
      </CardHeader>
      <CardContent>
        <Table className="w-full border-collapse">
          <TableHeader>
            <TableRow className="hover:bg-muted">
              <TableHead className="w-[200px] pb-2 text-left uppercase text-xs text-muted-foreground">Title</TableHead>
              <TableHead className="pb-2 text-left uppercase text-xs text-muted-foreground">Status</TableHead>
              <TableHead className="pb-2 text-left uppercase text-xs text-muted-foreground">Deadline</TableHead>
              <TableHead className="pb-2 text-left uppercase text-xs text-muted-foreground">Grade</TableHead>
              <TableHead className="pb-2 text-left uppercase text-xs text-muted-foreground text-right">Actions</TableHead>
            </TableRow>
          </TableHeader>
          <TableBody>
            {homeworkList.map((homework: Homework) => (
              <TableRow key={homework.id} className="hover:bg-muted/50 transition">
                <TableCell className="font-medium text-base">
                  <div>
                    <div className="font-semibold">{homework.title}</div>
                    <div className="text-sm text-gray-500">{homework.description}</div>
                  </div>
                </TableCell>
                <TableCell><StatusBadge status={homework.status} /></TableCell>
                <TableCell>{formatDate(homework.deadline)}</TableCell>
                <TableCell>
                  {homework.grade ? (
                    <span className="font-semibold text-green-700">{homework.grade.toFixed(2)}</span>
                  ) : (
                    <span className="text-gray-400">-</span>
                  )}
                </TableCell>
                <TableCell className="text-right">
                  {homework.filePath && (
                    <Button 
                      variant="outline" 
                      size="sm" 
                      onClick={() => handleDownload(homework.lessonId)}
                      className="flex items-center space-x-1"
                    >
                      <Download className="h-3 w-3" />
                      <span>Download</span>
                    </Button>
                  )}
                </TableCell>
              </TableRow>
            ))}
          </TableBody>
          <TableFooter>
            <TableRow className="hover:bg-muted">
              <TableCell colSpan={4} className="pt-2 border-t px-4 py-2">Pending Review</TableCell>
              <TableCell className="text-right pt-2 border-t px-4 py-2">{stats.submitted}</TableCell>
            </TableRow>
            <TableRow className="hover:bg-muted">
              <TableCell colSpan={4} className="pt-2 border-t px-4 py-2">Under Review</TableCell>
              <TableCell className="text-right pt-2 border-t px-4 py-2">{stats.reviewing}</TableCell>
            </TableRow>
            <TableRow className="hover:bg-muted">
              <TableCell colSpan={4} className="pt-2 border-t px-4 py-2">Reviewed</TableCell>
              <TableCell className="text-right pt-2 border-t px-4 py-2">{stats.reviewed}</TableCell>
            </TableRow>
            <TableRow className="hover:bg-muted">
              <TableCell colSpan={4} className="pt-2 border-t px-4 py-2">Returned</TableCell>
              <TableCell className="text-right pt-2 border-t px-4 py-2">{stats.returned}</TableCell>
            </TableRow>
          </TableFooter>
        </Table>
      </CardContent>
    </Card>
  );
}
