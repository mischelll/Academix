package com.academix.curriculumservice.service;

import com.academix.curriculumservice.dao.entity.Course;
import com.academix.curriculumservice.dao.repository.CourseRepository;
import com.academix.curriculumservice.service.dto.course.CourseDTO;
import com.academix.curriculumservice.service.dto.course.CreateCourseRequest;
import com.academix.curriculumservice.service.mapper.CourseMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CourseService {

    private final CourseRepository courseRepository;
    private final CourseMapper courseMapper;

    public CourseDTO create(CreateCourseRequest request) {
        Course course = courseMapper.fromCreateRequest(request);
        course = courseRepository.save(course);
        return courseMapper.toDto(course);
    }

    public List<Course> findAll() {
        return courseRepository.findAll();
    }

    public Course findById(Long id) {
        return courseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Course not found"));
    }

    public Course update(Long id, Course updatedCourse) {
        Course existing = findById(id);
        existing.setName(updatedCourse.getName());
        existing.setSemester(updatedCourse.getSemester());
        return courseRepository.save(existing);
    }

    public void delete(Long id) {
        courseRepository.deleteById(id);
    }
}
