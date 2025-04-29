package com.academix.curriculumservice.web;

import com.academix.curriculumservice.dao.entity.CourseTeacher;
import com.academix.curriculumservice.service.CourseTeacherService;
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
    public ResponseEntity<CourseTeacher> create(@RequestBody CourseTeacher courseTeacher) {
        return ResponseEntity.ok(service.create(courseTeacher));
    }

    @GetMapping
    public ResponseEntity<List<CourseTeacher>> findAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CourseTeacher> findById(@PathVariable Long id) {
        return service.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
