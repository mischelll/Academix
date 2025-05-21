package com.academix.homeworkservice.service.apiclients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "curriculumClient", url = "${services.curriculum.url}")
public interface CurriculumClient {

    @GetMapping("/curriculum/course-teachers/internal/teachers/check")
    boolean isTeacherOfCourse(@RequestParam("userId") Long userId,
                              @RequestParam("lessonId") Long lessonId);
}