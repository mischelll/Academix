package com.academix.curriculumservice.service.dto.lesson;

public record LessonDTO(
        Long id,
        String title,
        String description,
        Long courseId
) {
}
