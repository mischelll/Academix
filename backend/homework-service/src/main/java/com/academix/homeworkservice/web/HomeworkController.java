package com.academix.homeworkservice.web;

import com.academix.homeworkservice.dao.entity.Homework;
import com.academix.homeworkservice.service.HomeworkService;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/homeworks")
public class HomeworkController {

    record Homework(String title, String content) {}

    public record HomeworkDTO(String title, String content, String fileKey, Long studentId) {}

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

    @PostMapping
    public ResponseEntity<com.academix.homeworkservice.dao.entity.Homework> createHomework(@RequestBody HomeworkDTO homework) {
        logger.info("Creating homework: {}", homework);
        com.academix.homeworkservice.dao.entity.Homework homework1 = homeworkService.createHomework(homework);
        return ResponseEntity.created(URI.create("/api/homeworks"))
                .body(homework1);
    }
}
