package com.academix.curriculumservice.service.apiclient.homework;

public record HomeworkMetaDTO(
        Long id,
        Long lessonId,
        Long studentId,
        Long endTime
) {}
