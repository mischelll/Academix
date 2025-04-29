package com.academix.curriculumservice.web;

import com.academix.curriculumservice.dao.entity.Course;
import com.academix.curriculumservice.service.CourseService;
import com.academix.curriculumservice.service.dto.course.CourseDTO;
import com.academix.curriculumservice.service.dto.course.CreateCourseRequest;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/courses")
public class CourseController {
    private final CourseService courseService;

    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    @GetMapping
    public List<Course> getAllCourses() {
        return courseService.findAll();
    }

    @GetMapping("/{id}")
    public Course getCourse(@PathVariable Long id) {
        return courseService.findById(id);
    }

    @PostMapping
    public CourseDTO createCourse(@RequestBody CreateCourseRequest createCourseRequest) {
        return courseService.create(createCourseRequest);
    }

    @PutMapping("/{id}")
    public Course updateCourse(@PathVariable Long id, @RequestBody Course course) {
        return courseService.update(id, course);
    }

    @DeleteMapping("/{id}")
    public void deleteCourse(@PathVariable Long id) {
        courseService.delete(id);
    }
}
