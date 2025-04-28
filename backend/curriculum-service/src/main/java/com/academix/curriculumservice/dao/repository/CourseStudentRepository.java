package com.academix.curriculumservice.dao.repository;

import com.academix.curriculumservice.dao.entity.CourseStudent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CourseStudentRepository extends JpaRepository<CourseStudent, Long> {

    List<CourseStudent> findByCourseId(Long courseId);

    List<CourseStudent> findByStudentId(Long studentId);

}
