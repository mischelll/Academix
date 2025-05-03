package com.academix.curriculumservice.service.dto.course_teacher;

public record AssignTeacherCourseRequest(
        Long courseId,
        Long teacherId
) {}