package com.academix.curriculumservice.web;

import com.academix.curriculumservice.service.CourseTeacherService;
import com.academix.curriculumservice.service.dto.course_teacher.AssignTeacherCourseRequest;
import com.academix.curriculumservice.service.dto.course_teacher.CourseTeacherDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/curriculum/course-teachers")
public class CourseTeacherController {
    private final CourseTeacherService service;

    public CourseTeacherController(CourseTeacherService service) {
        this.service = service;
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<CourseTeacherDTO> create(@RequestBody AssignTeacherCourseRequest assignTeacherCourseRequest) {
        return ResponseEntity.ok(service.assignTeacher(assignTeacherCourseRequest));
    }

    @GetMapping
    public ResponseEntity<List<CourseTeacherDTO>> findAll() {
        return ResponseEntity.ok(service.getAllAssignments());
    }

    @GetMapping("/course/{courseId}")
    public ResponseEntity<CourseTeacherDTO> findById(@PathVariable Long courseId) {
        return ResponseEntity.ok(service.findTeacherByCourse(courseId));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/internal/teachers/check")
    public boolean isTeacherOfCourse(@RequestParam Long userId, @RequestParam Long lessonId) {
        return service.isTeacherForLesson(userId, lessonId);
    }

    @GetMapping("/internal/teachers/{teacherId}/lessons")
    public ResponseEntity<List<Long>> getTeacherLessonIds(@PathVariable Long teacherId) {
        return ResponseEntity.ok(service.getTeacherLessonIds(teacherId));
    }
}
