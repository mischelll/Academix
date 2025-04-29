package com.academix.curriculumservice.service;

import com.academix.curriculumservice.dao.entity.CourseStudent;
import com.academix.curriculumservice.dao.repository.CourseStudentRepository;
import com.academix.curriculumservice.service.dto.course_student.AssignStudentCourseRequest;
import com.academix.curriculumservice.service.dto.course_student.CourseStudentDTO;
import com.academix.curriculumservice.service.mapper.CourseStudentMapper;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CourseStudentService {

    private final static Logger LOGGER = LoggerFactory.getLogger(CourseStudentService.class);

    private final CourseStudentRepository repository;
    private final CourseStudentMapper mapper;

    public CourseStudentDTO assignStudent(AssignStudentCourseRequest request) {
        CourseStudent courseStudent = mapper.fromCreateRequest(request);
        return mapper.toDto(repository.save(courseStudent));
    }

    public List<CourseStudentDTO> getAllAssignments() {
        return repository.findAll()
                .stream()
                .map(mapper::toDto)
                .toList();
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }
}
