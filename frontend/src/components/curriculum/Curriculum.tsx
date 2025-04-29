import { useEffect } from "react";
// import {
//   Table,
//   TableHeader,
//   TableRow,
//   TableHead,
//   TableBody,
//   TableCell,
//   TableFooter,
// } from "../ui/table";
import { fetchCurriculum } from "@/api/curriculum";

import { Dialog, DialogContent, DialogDescription, DialogTitle, DialogTrigger } from "@radix-ui/react-dialog";
import { DialogHeader } from "../ui/dialog";

export default function Curriculum() {
  useEffect(() => {
    fetchCurriculum()
      .then(() => {
        console.log("SUCCESS");
      })
      .catch(() => {
        console.error("ERROR");
      });
  }, []);

  return (
    <>
    <Dialog>
  <DialogTrigger>Open</DialogTrigger>
  <DialogContent>
    <DialogHeader>
      <DialogTitle>Are you absolutely sure?</DialogTitle>
      <DialogDescription>
        This action cannot be undone. This will permanently delete your account
        and remove your data from our servers.
      </DialogDescription>
    </DialogHeader>
  </DialogContent>
</Dialog>
{/* 
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
        

    </>
  );
}
