import { useEffect } from "react";
import {Table, TableHeader, TableRow, TableHead, TableBody, TableCell, TableFooter } from "../ui/table";
import { fetchCurriculum } from "@/api/curriculum";

export default function Curriculum() {

    useEffect(() => {
        fetchCurriculum()
        .then(() => {
            console.log("SUCCESS")
          })
          .catch(() => {
            console.error("ERROR")
          });
    }, [])

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
        {/* {homeworks.map((homework) => (
          <TableRow key={homework.fileKey}>
            <TableCell className="font-medium">{homework.title}</TableCell>
            <TableCell>{homework.status}</TableCell>
            <TableCell>{homework.studentId}</TableCell>
            <TableCell className="text-right">
                <Link to="">{homework.fileKey}</Link>
                </TableCell>
          </TableRow>
        ))} */}
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
    )

}