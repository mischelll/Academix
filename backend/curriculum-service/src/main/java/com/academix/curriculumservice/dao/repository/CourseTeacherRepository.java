package com.academix.curriculumservice.dao.repository;

import com.academix.curriculumservice.dao.entity.CourseTeacher;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CourseTeacherRepository extends JpaRepository<CourseTeacher, Long> {

    List<CourseTeacher> findByCourseId(Long courseId);

    List<CourseTeacher> findByTeacherId(Long teacherId);
}
