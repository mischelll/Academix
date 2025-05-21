package com.academix.homeworkservice.web;

import com.academix.homeworkservice.service.HomeworkService;
import com.academix.homeworkservice.service.dto.HomeworkMetaDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/homeworks")
public class HomeworkController {

    record Homework(String title, String content) {}

    public record HomeworkDTO(String title, String content, String fileKey, Long studentId, Long lessonId) {}

    private static final Logger logger = LoggerFactory.getLogger(HomeworkController.class);

    private final HomeworkService homeworkService;

    public HomeworkController(HomeworkService homeworkService) {
        this.homeworkService = homeworkService;
    }

    @GetMapping("/current/{id}")
    public ResponseEntity<Homework> getHomeworkById(@PathVariable Long id) {
        logger.info("Getting homework by id: {}", id);
        return ResponseEntity.ok(new Homework("Math III", "f(x) = 0"));
    }

    @GetMapping("/student/{studentId}")
    public ResponseEntity<Page<com.academix.homeworkservice.dao.entity.Homework>> getHomeworksForStudent(@PathVariable Long studentId) {
        logger.info("getHomeworksForStudent");
        return ResponseEntity.ok(homeworkService.getAllHomeworksForStudent(studentId));
    }

    @GetMapping("/internal/lessons/{lessonId}")
    public ResponseEntity<HomeworkMetaDTO> getHomeworkForLesson(@PathVariable Long lessonId) {
        logger.info("getHomeworkForLesson");
        return ResponseEntity.ok(homeworkService.getHomeworkByLessonId(lessonId));
    }

    @GetMapping("/internal/lessons/batch")
    public ResponseEntity<List<HomeworkMetaDTO>> getHomeworkForLessons(@RequestParam List<Long> lessonIds) {
        logger.info("getHomeworkForLessons");
        return ResponseEntity.ok(homeworkService.getHomeworkByLessonIds(lessonIds));
    }

    @PostMapping
    public ResponseEntity<com.academix.homeworkservice.dao.entity.Homework> createHomework(@RequestBody HomeworkDTO homework) {
        logger.info("Creating homework: {}", homework);
        com.academix.homeworkservice.dao.entity.Homework homework1 = homeworkService.createHomework(homework);
        return ResponseEntity.created(URI.create("/api/homeworks"))
                .body(homework1);
    }

    @GetMapping("/{lessonId}/download-url")
    public ResponseEntity<String> getDownloadUrl(@PathVariable Long lessonId, Principal principal) {
        return ResponseEntity
                .status(HttpStatusCode.valueOf(200))
                .body(homeworkService.getDownloadUrl(lessonId, principal));
    }
}
