package com.academix.curriculumservice.web;

import com.academix.curriculumservice.dao.entity.Course;
import com.academix.curriculumservice.service.CourseService;
import com.academix.curriculumservice.service.dto.course.CourseDTO;
import com.academix.curriculumservice.service.dto.course.CreateCourseRequest;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/curriculum/courses")
public class CourseController {
    private final CourseService courseService;

    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    @GetMapping
    public List<CourseDTO> getAllCourses() {
        return courseService.getAllCourses();
    }

    @GetMapping("/{id}")
    public CourseDTO getCourse(@PathVariable Long id) {
        return courseService.getById(id);
    }

    @PostMapping
    public CourseDTO createCourse(@RequestBody CreateCourseRequest createCourseRequest) {
        return courseService.create(createCourseRequest);
    }

//    @PutMapping("/{id}")
//    public CourseDTO updateCourse(@PathVariable Long id, @RequestBody Course course) {
//        return courseService.update(id, course);
//    }

    @DeleteMapping("/{id}")
    public void deleteCourse(@PathVariable Long id) {
        courseService.delete(id);
    }
}
