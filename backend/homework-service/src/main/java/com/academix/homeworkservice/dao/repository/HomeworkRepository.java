package com.academix.homeworkservice.dao.repository;

import com.academix.homeworkservice.dao.entity.Homework;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HomeworkRepository extends JpaRepository<Homework, Long> {

    Page<Homework> findAllByStudentId(Long studentId, Pageable pageable);
}
