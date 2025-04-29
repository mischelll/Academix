package com.academix.curriculumservice.service;

import com.academix.curriculumservice.dao.entity.CourseStudent;
import com.academix.curriculumservice.dao.repository.CourseStudentRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CourseStudentService {

    private final static Logger LOGGER = LoggerFactory.getLogger(CourseStudentService.class);

    private final CourseStudentRepository repository;

    public CourseStudent create(CourseStudent courseStudent) {
        return repository.save(courseStudent);
    }

    public List<CourseStudent> findAll() {
        return repository.findAll();
    }

    public Optional<CourseStudent> findById(Long id) {
        return repository.findById(id);
    }

    public void deleteById(Long id) {
        repository.deleteById(id);
    }
}
