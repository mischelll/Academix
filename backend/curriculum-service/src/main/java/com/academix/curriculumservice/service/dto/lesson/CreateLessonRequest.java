package com.academix.curriculumservice.service.dto.lesson;

public record CreateLessonRequest(
        String title,
        String description,
        Long courseId
) {}