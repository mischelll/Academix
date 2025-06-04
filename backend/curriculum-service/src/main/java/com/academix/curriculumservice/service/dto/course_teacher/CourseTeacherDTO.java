package com.academix.curriculumservice.service.dto.course_teacher;

public record CourseTeacherDTO(
        Long id,
        Long courseId,
        Long teacherId,
        String teacherName,
        String teacherEmail
) {}
