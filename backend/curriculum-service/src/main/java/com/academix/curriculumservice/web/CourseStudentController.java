package com.academix.curriculumservice.web;

import com.academix.curriculumservice.dao.entity.CourseStudent;
import com.academix.curriculumservice.service.CourseStudentService;
import com.academix.curriculumservice.service.dto.course_student.AssignStudentCourseRequest;
import com.academix.curriculumservice.service.dto.course_student.CourseStudentDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/curriculum/course-students")
public class CourseStudentController {

    private final CourseStudentService service;

    public CourseStudentController(CourseStudentService service) {
        this.service = service;
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    public ResponseEntity<CourseStudentDTO> create(@RequestBody AssignStudentCourseRequest assignStudentCourseRequest) {
        return ResponseEntity.ok(service.assignStudent(assignStudentCourseRequest));
    }

    @GetMapping
    public ResponseEntity<List<CourseStudentDTO>> findAll() {
        return ResponseEntity.ok(service.getAllAssignments());
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
