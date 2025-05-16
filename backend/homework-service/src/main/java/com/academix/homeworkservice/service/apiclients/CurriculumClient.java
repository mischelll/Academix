package com.academix.homeworkservice.service.apiclients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "curriculum-service")
public interface CurriculumClient {
    record LessonDTO(
            Long id,
            String title,
            String description,
            Long courseId,
            Long studentId
    ) {}

    @GetMapping("/api/curriculum/lessons/{id}")
    LessonDTO getLesson(@PathVariable("id") Long lessonId);
}


