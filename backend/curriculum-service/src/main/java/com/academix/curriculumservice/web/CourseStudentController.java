package com.academix.curriculumservice.web;

import com.academix.curriculumservice.dao.entity.CourseStudent;
import com.academix.curriculumservice.service.CourseStudentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/course-students")
public class CourseStudentController {

    private final CourseStudentService service;

    public CourseStudentController(CourseStudentService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<CourseStudent> create(@RequestBody CourseStudent courseStudent) {
        return ResponseEntity.ok(service.create(courseStudent));
    }

    @GetMapping
    public ResponseEntity<List<CourseStudent>> findAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CourseStudent> findById(@PathVariable Long id) {
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
