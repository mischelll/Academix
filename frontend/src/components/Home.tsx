import { Tabs, TabsList, TabsTrigger, TabsContent } from '../components/ui/tabs';
import { useQuery } from '@tanstack/react-query';
import { fetchUserHomeworks, Homework, HomeworkStatus } from '../api/homework';
import { useUserStore } from '../stores/userStore';
import { Table, TableHeader, TableRow, TableHead, TableBody, TableCell } from "./ui/table";
import { Card, CardHeader, CardTitle, CardContent } from "./ui/card";
import { Badge } from "./ui/badge";
import { Check, Clock, X, FileText } from "lucide-react";

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

export default function Home() {
  const user = useUserStore((state) => state.user);
  const { data: homeworks, isLoading, error } = useQuery({
    queryKey: ["userHomeworks", user?.id],
    queryFn: () => fetchUserHomeworks(user!.id),
    enabled: !!user?.id,
  });

  const homeworkList: Homework[] = homeworks?.content || homeworks || [];

  const submitted = homeworkList.filter(
    (hw) =>
      hw.status === HomeworkStatus.SUBMITTED ||
      hw.status === HomeworkStatus.REVIEWING ||
      hw.status === HomeworkStatus.REVIEWED ||
      hw.status === HomeworkStatus.RETURNED
  );
  const unsubmitted = homeworkList.filter((hw) => hw.status === HomeworkStatus.OPEN);

  return (
    <Card className="w-full max-w-3xl mx-auto mt-10 shadow-lg border">
      <CardHeader>
        <CardTitle className="text-2xl font-bold">My Assignments Overview</CardTitle>
      </CardHeader>
      <CardContent>
        <Tabs defaultValue="submitted" className="w-full">
          <TabsList className="mb-4">
            <TabsTrigger value="submitted">Submitted</TabsTrigger>
            <TabsTrigger value="unsubmitted">Unsubmitted</TabsTrigger>
          </TabsList>
          <TabsContent value="submitted">
            {isLoading ? (
              <div className="p-6">Loading...</div>
            ) : error ? (
              <div className="p-6 text-red-500">Error loading assignments</div>
            ) : submitted.length === 0 ? (
              <EmptyState text="No submitted assignments." />
            ) : (
              <Table>
                <TableHeader>
                  <TableRow>
                    <TableHead>Title</TableHead>
                    <TableHead>Status</TableHead>
                    <TableHead>Deadline</TableHead>
                    <TableHead>Grade</TableHead>
                  </TableRow>
                </TableHeader>
                <TableBody>
                  {submitted.map((hw) => (
                    <TableRow key={hw.id} className="hover:bg-muted/50 transition">
                      <TableCell className="font-medium text-base">{hw.title}</TableCell>
                      <TableCell><StatusBadge status={hw.status} /></TableCell>
                      <TableCell>{hw.deadline ? new Date(hw.deadline).toLocaleDateString() : '-'}</TableCell>
                      <TableCell>
                        {hw.grade ? (
                          <span className="font-semibold text-green-700">{hw.grade.toFixed(2)}</span>
                        ) : (
                          <span className="text-gray-400">-</span>
                        )}
                      </TableCell>
                    </TableRow>
                  ))}
                </TableBody>
              </Table>
            )}
          </TabsContent>
          <TabsContent value="unsubmitted">
            {isLoading ? (
              <div className="p-6">Loading...</div>
            ) : error ? (
              <div className="p-6 text-red-500">Error loading assignments</div>
            ) : unsubmitted.length === 0 ? (
              <EmptyState text="No unsubmitted assignments." />
            ) : (
              <Table>
                <TableHeader>
                  <TableRow>
                    <TableHead>Title</TableHead>
                    <TableHead>Status</TableHead>
                    <TableHead>Deadline</TableHead>
                  </TableRow>
                </TableHeader>
                <TableBody>
                  {unsubmitted.map((hw) => (
                    <TableRow key={hw.id} className="hover:bg-muted/50 transition">
                      <TableCell className="font-medium text-base">{hw.title}</TableCell>
                      <TableCell><StatusBadge status={hw.status} /></TableCell>
                      <TableCell>{hw.deadline ? new Date(hw.deadline).toLocaleDateString() : '-'}</TableCell>
                    </TableRow>
                  ))}
                </TableBody>
              </Table>
            )}
          </TabsContent>
        </Tabs>
      </CardContent>
    </Card>
  );
}
