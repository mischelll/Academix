package com.academix.curriculumservice.dao.repository;

import com.academix.curriculumservice.dao.entity.Course;
import com.academix.curriculumservice.dao.entity.Semester;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CourseRepository extends JpaRepository<Course, Long> {
    List<Course> findAllBySemester(Semester semester);
}
