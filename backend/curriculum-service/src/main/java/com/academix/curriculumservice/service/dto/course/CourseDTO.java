package com.academix.curriculumservice.service.dto.course;

public record CourseDTO (
        Long id,
        String name,
        Long semesterId
) {}