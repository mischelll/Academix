package com.academix.curriculumservice.dao.repository;

import com.academix.curriculumservice.dao.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseRepository extends JpaRepository<Course, Long> {
}
