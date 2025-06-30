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

export default function Homework() {
  const user = useUserStore((state) => state.user);

  const { data: homeworks, isLoading, error } = useQuery({
    queryKey: ["userHomeworks", user?.id],
    queryFn: () => fetchUserHomeworks(user!.id),
    enabled: !!user?.id,
  });

  const handleDownload = async (lessonId: number) => {
    try {
      await downloadHomework(lessonId);
    } catch (error) {
      console.error("Failed to download homework:", error);
    }
  };

  const getStatusIcon = (status: HomeworkStatus) => {
    switch (status) {
      case HomeworkStatus.REVIEWED:
        return <Check className="h-4 w-4 text-green-500" />;
      case HomeworkStatus.SUBMITTED:
        return <Clock className="h-4 w-4 text-blue-500" />;
      case HomeworkStatus.REVIEWING:
        return <Clock className="h-4 w-4 text-yellow-500" />;
      case HomeworkStatus.RETURNED:
        return <X className="h-4 w-4 text-red-500" />;
      case HomeworkStatus.OPEN:
        return <FileText className="h-4 w-4 text-gray-500" />;
      default:
        return <FileText className="h-4 w-4 text-gray-500" />;
    }
  };

  const getStatusColor = (status: HomeworkStatus) => {
    switch (status) {
      case HomeworkStatus.REVIEWED:
        return "text-green-600";
      case HomeworkStatus.SUBMITTED:
        return "text-blue-600";
      case HomeworkStatus.REVIEWING:
        return "text-yellow-600";
      case HomeworkStatus.RETURNED:
        return "text-red-600";
      case HomeworkStatus.OPEN:
        return "text-gray-600";
      default:
        return "text-gray-600";
    }
  };

  const getStatusDisplayText = (status: HomeworkStatus) => {
    switch (status) {
      case HomeworkStatus.SUBMITTED:
        return "Pending Review";
      case HomeworkStatus.REVIEWING:
        return "Under Review";
      case HomeworkStatus.REVIEWED:
        return "Reviewed";
      case HomeworkStatus.RETURNED:
        return "Returned";
      case HomeworkStatus.OPEN:
        return "Open";
      default:
        return status;
    }
  };

  const formatDate = (dateString: string) => {
    try {
      return format(new Date(dateString), "MMM dd, yyyy");
    } catch {
      return "Invalid date";
    }
  };

  if (isLoading) {
    return (
      <div className="flex items-center justify-center h-64">
        <div className="text-lg">Loading homeworks...</div>
      </div>
    );
  }

  if (error) {
    return (
      <div className="flex items-center justify-center h-64">
        <div className="text-lg text-red-600">Error loading homeworks</div>
      </div>
    );
  }

  if (!homeworks || homeworks.content?.length === 0) {
    return (
      <div className="flex items-center justify-center h-64">
        <div className="text-lg text-gray-600">No homeworks found</div>
      </div>
    );
  }

  const homeworkList = homeworks.content || homeworks;

  // Calculate statistics
  const stats = {
    submitted: homeworkList.filter((h: Homework) => h.status === HomeworkStatus.SUBMITTED).length,
    reviewing: homeworkList.filter((h: Homework) => h.status === HomeworkStatus.REVIEWING).length,
    reviewed: homeworkList.filter((h: Homework) => h.status === HomeworkStatus.REVIEWED).length,
    returned: homeworkList.filter((h: Homework) => h.status === HomeworkStatus.RETURNED).length,
  };

  return (
    <div className="space-y-6">
      <div className="flex justify-between items-center">
        <h1 className="text-2xl font-bold">My Homework Assignments</h1>
      </div>
      
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
            <TableRow key={homework.id} className="hover:bg-muted">
              <TableCell className="font-medium border-b px-4 py-2">
                <div>
                  <div className="font-semibold">{homework.title}</div>
                  <div className="text-sm text-gray-500">{homework.description}</div>
                </div>
              </TableCell>
              <TableCell className="border-b px-4 py-2">
                <div className="flex items-center space-x-2">
                  {getStatusIcon(homework.status)}
                  <span className={getStatusColor(homework.status)}>
                    {getStatusDisplayText(homework.status)}
                  </span>
                </div>
              </TableCell>
              <TableCell className="border-b px-4 py-2">
                {formatDate(homework.deadline)}
              </TableCell>
              <TableCell className="border-b px-4 py-2">
                {homework.grade ? (
                  <span className="font-semibold">{homework.grade.toFixed(2)}</span>
                ) : (
                  <span className="text-gray-400">-</span>
                )}
              </TableCell>
              <TableCell className="text-right border-b px-4 py-2">
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
    </div>
  );
}
