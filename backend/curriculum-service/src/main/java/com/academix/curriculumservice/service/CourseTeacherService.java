package com.academix.curriculumservice.service;

import com.academix.curriculumservice.dao.entity.CourseTeacher;
import com.academix.curriculumservice.dao.repository.CourseTeacherRepository;
import com.academix.curriculumservice.service.dto.course_teacher.AssignTeacherCourseReques;
import com.academix.curriculumservice.service.dto.course_teacher.CourseTeacherDTO;
import com.academix.curriculumservice.service.mapper.CourseTeacherMapper;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CourseTeacherService {

    private final static Logger LOGGER = LoggerFactory.getLogger(CourseTeacherService.class);

    private final CourseTeacherRepository repository;
    private final CourseTeacherMapper mapper;

    public CourseTeacherDTO assignTeacher(AssignTeacherCourseReques request) {
        CourseTeacher courseTeacher = mapper.fromCreateRequest(request);
        return mapper.toDto(repository.save(courseTeacher));
    }

    public List<CourseTeacherDTO> getAllAssignments() {
        return repository.findAll()
                .stream()
                .map(mapper::toDto)
                .toList();
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }
}

