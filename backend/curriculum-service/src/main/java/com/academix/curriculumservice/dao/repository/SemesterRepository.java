package com.academix.curriculumservice.dao.repository;

import com.academix.curriculumservice.dao.entity.Major;
import com.academix.curriculumservice.dao.entity.Semester;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SemesterRepository extends JpaRepository<Semester, Long> {

    List<Semester> findAllByMajor(Major major);
}
