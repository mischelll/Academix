package com.academix.curriculumservice.service;

import com.academix.curriculumservice.dao.entity.CourseTeacher;
import com.academix.curriculumservice.dao.repository.CourseTeacherRepository;
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

    public CourseTeacher create(CourseTeacher courseTeacher) {
        return repository.save(courseTeacher);
    }

    public List<CourseTeacher> findAll() {
        return repository.findAll();
    }

    public Optional<CourseTeacher> findById(Long id) {
        return repository.findById(id);
    }

    public void deleteById(Long id) {
        repository.deleteById(id);
    }
}

