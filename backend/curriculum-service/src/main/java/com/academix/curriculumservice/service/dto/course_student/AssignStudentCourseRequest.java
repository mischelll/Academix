package com.academix.curriculumservice.service.dto.course_student;

public record AssignStudentCourseRequest(
        Long courseId,
        Long studentId
) {}