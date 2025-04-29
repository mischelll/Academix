package com.academix.curriculumservice.service.dto.course_student;

public record CourseStudentDTO(
        Long id,
        Long courseId,
        Long studentId
) {}