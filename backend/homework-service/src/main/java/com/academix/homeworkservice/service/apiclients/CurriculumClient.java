package com.academix.homeworkservice.service.apiclients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "curriculumClient", url = "${services.curriculum.url}")
public interface CurriculumClient {

    @GetMapping("/curriculum/course-teachers/internal/teachers/check")
    boolean isTeacherOfCourse(@RequestParam("userId") Long userId,
                              @RequestParam("lessonId") Long lessonId);

    @GetMapping("/curriculum/course-teachers/internal/teachers/{teacherId}/lessons")
    List<Long> getTeacherLessonIds(@PathVariable("teacherId") Long teacherId);

    @GetMapping("/curriculum/lessons/internal/lessons/{lessonId}")
    LessonInfo getLessonById(@PathVariable("lessonId") Long lessonId);

    record LessonInfo(Long id, String title, String description, Long courseId, Long endTime) {}
}