package com.academix.curriculumservice.service;

import com.academix.curriculumservice.dao.entity.CourseStudent;
import com.academix.curriculumservice.dao.repository.CourseStudentRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CourseStudentService {

    private final static Logger LOGGER = LoggerFactory.getLogger(CourseStudentService.class);

    private final CourseStudentRepository courseStudentRepository;


    public List<CourseStudent> getStudentsForCourse(Long courseId) {
        return courseStudentRepository.findByCourseId(courseId);
    }

    public void assignStudentToCourse(Long courseId, Long studentId) {
        CourseStudent assignment = CourseStudent.builder()
                .courseId(courseId)
                .studentId(studentId)
                .build();
        courseStudentRepository.save(assignment);
    }

    public void removeStudentFromCourse(Long courseId, Long studentId) {
        List<CourseStudent> assignments = courseStudentRepository.findByCourseId(courseId);
        assignments.stream()
                .filter(a -> a.getStudentId().equals(studentId))
                .forEach(courseStudentRepository::delete);
    }
}
