package com.academix.notificationservice.client;

import com.academix.notificationservice.config.FeignConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "curriculumServiceClient", url = "${services.curriculum.url}", configuration = FeignConfig.class)
public interface CurriculumServiceFeignClient {

    @GetMapping("/api/curriculum/course-teachers/internal/lesson/{lessonId}/teacher")
    TeacherEmailResponse getTeacherByLessonId(@PathVariable("lessonId") Long lessonId);

    record TeacherEmailResponse(Long teacherId, String teacherName, String teacherEmail) {}
} 