package com.academix.curriculumservice.service.apiclient.homework;

import com.academix.curriculumservice.config.FeignConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Set;

@FeignClient(name = "homework-service", url = "${services.homework.url}", configuration = FeignConfig.class)
public interface HomeworkServiceClient {

    @GetMapping("/homeworks/internal/lessons/{lessonId}")
    HomeworkMetaDTO getHomeworkMetaByLessonId(Long lessonId);

    @GetMapping("/homeworks/internal/lessons/batch")
    Set<HomeworkMetaDTO> getHomeworkMetaByLessonIds(@RequestParam Set<Long> lessonIds);
}
