package com.academix.curriculumservice.service;

import com.academix.curriculumservice.dao.entity.Course;
import com.academix.curriculumservice.dao.entity.Semester;
import com.academix.curriculumservice.dao.repository.CourseRepository;
import com.academix.curriculumservice.dao.repository.SemesterRepository;
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
    private final SemesterRepository semesterRepository;
    private final CourseMapper courseMapper;

    public CourseDTO create(CreateCourseRequest request) {
        Course course = courseMapper.fromCreateRequest(request);
        return courseMapper.toDto(courseRepository.save(course));
    }

    public CourseDTO getById(Long id) {
        return courseRepository.findById(id)
                .map(courseMapper::toDto)
                .orElseThrow(() -> new RuntimeException("Course not found"));
    }

    public List<CourseDTO> getAllCourses() {
        return courseRepository.findAll()
                .stream()
                .map(courseMapper::toDto)
                .toList();
    }

    public void delete(Long id) {
        courseRepository.deleteById(id);
    }

    public List<CourseDTO> getAllCoursesBySemester(Long semesterId) {
        Semester semester = semesterRepository.findById(semesterId)
                .orElseThrow(() -> new RuntimeException("Semester not found"));

        return courseRepository.findAllBySemester(semester)
                .stream()
                .map(courseMapper::toDto)
                .toList();

    }
}
