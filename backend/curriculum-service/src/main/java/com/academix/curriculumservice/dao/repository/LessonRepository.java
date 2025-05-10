package com.academix.curriculumservice.dao.repository;

import com.academix.curriculumservice.dao.entity.Course;
import com.academix.curriculumservice.dao.entity.Lesson;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface LessonRepository extends JpaRepository<Lesson, Long> {
    List<Lesson> findAllByCourse(Course course);
}
