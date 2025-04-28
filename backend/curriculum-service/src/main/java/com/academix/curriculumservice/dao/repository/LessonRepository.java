package com.academix.curriculumservice.dao.repository;

import com.academix.curriculumservice.dao.entity.Lesson;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LessonRepository extends JpaRepository<Lesson, Long> {
}
