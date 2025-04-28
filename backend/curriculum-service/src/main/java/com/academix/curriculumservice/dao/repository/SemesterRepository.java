package com.academix.curriculumservice.dao.repository;

import com.academix.curriculumservice.dao.entity.Semester;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SemesterRepository extends JpaRepository<Semester, Long> {
}
