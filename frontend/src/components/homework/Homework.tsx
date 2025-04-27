import { Link } from "react-router-dom";
import {
  Table,
  TableHeader,
  TableRow,
  TableHead,
  TableBody,
  TableCell,
  TableFooter,
} from "../ui/table";

export default function Homework() {
  const homeworks = [
    {
      title: "HMWRK1",
      status: "REVIEWED",
      studentId: 13,
      fileKey: "Credit Card2.pdf",
    },
    {
      title: "HMWRK2",
      status: "Submitted",
      studentId: 11,
      fileKey: "Credit Card.pdf",
    },
  ];

  return (
    <Table>
      <TableHeader>
        <TableRow>
          <TableHead className="w-[100px]">Title</TableHead>
          <TableHead>Status</TableHead>
          <TableHead>Student</TableHead>
          <TableHead className="text-right">File</TableHead>
        </TableRow>
      </TableHeader>
      <TableBody>
        {homeworks.map((homework) => (
          <TableRow key={homework.fileKey}>
            <TableCell className="font-medium">{homework.title}</TableCell>
            <TableCell>{homework.status}</TableCell>
            <TableCell>{homework.studentId}</TableCell>
            <TableCell className="text-right">
                <Link to="">{homework.fileKey}</Link>
                </TableCell>
          </TableRow>
        ))}
      </TableBody>
      <TableFooter>
        <TableRow>
          <TableCell colSpan={3}>Submitted</TableCell>
          <TableCell className="text-right">1</TableCell>
        </TableRow>
        <TableRow>
          <TableCell colSpan={3}>Under Review</TableCell>
          <TableCell className="text-right">2</TableCell>
        </TableRow>
        <TableRow>
          <TableCell colSpan={3}>Reviewed</TableCell>
          <TableCell className="text-right">1</TableCell>
        </TableRow>
        <TableRow>
          <TableCell colSpan={3}>Returned</TableCell>
          <TableCell className="text-right">0</TableCell>
        </TableRow>
      </TableFooter>
    </Table>
  );
}
