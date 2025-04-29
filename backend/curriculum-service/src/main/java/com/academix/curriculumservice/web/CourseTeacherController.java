package com.academix.curriculumservice.web;

import com.academix.curriculumservice.dao.entity.CourseTeacher;
import com.academix.curriculumservice.service.CourseTeacherService;
import com.academix.curriculumservice.service.dto.course_teacher.AssignTeacherCourseReques;
import com.academix.curriculumservice.service.dto.course_teacher.CourseTeacherDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/course-teachers")
public class CourseTeacherController {
    private final CourseTeacherService service;

    public CourseTeacherController(CourseTeacherService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<CourseTeacherDTO> create(@RequestBody AssignTeacherCourseReques assignTeacherCourseReques) {
        return ResponseEntity.ok(service.assignTeacher(assignTeacherCourseReques));
    }

    @GetMapping
    public ResponseEntity<List<CourseTeacherDTO>> findAll() {
        return ResponseEntity.ok(service.getAllAssignments());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
