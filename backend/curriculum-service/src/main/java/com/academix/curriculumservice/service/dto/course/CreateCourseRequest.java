package com.academix.curriculumservice.service.dto.course;

public record CreateCourseRequest(
        String name,
        Long semesterId
) {}