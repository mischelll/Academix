package com.academix.homeworkservice.web;

import com.academix.homeworkservice.service.HomeworkService;
import com.academix.homeworkservice.service.dto.GradeHomeworkRequest;
import com.academix.homeworkservice.service.dto.HomeworkMetaDTO;
import com.academix.homeworkservice.service.dto.TeacherHomeworkDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/homeworks")
public class HomeworkController {

    record Homework(String title, String content) {}

    public record HomeworkDTO(String title, String content, String filePath, Long studentId, Long lessonId, String description, Long credits) {}

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

    @PostMapping("/internal")
    public ResponseEntity<com.academix.homeworkservice.dao.entity.Homework> createHomeworkInternal(@RequestBody HomeworkDTO homework) {
        logger.info("Creating homework (internal): {}", homework);
        com.academix.homeworkservice.dao.entity.Homework homework1 = homeworkService.createHomework(homework);
        return ResponseEntity.created(URI.create("/api/homeworks/internal")).body(homework1);
    }

    @GetMapping("/{lessonId}/download-url")
    public ResponseEntity<String> getDownloadUrl(@PathVariable Long lessonId, Principal principal) {
        return ResponseEntity
                .status(HttpStatusCode.valueOf(200))
                .body(homeworkService.getDownloadUrl(lessonId, principal));
    }

    @GetMapping("/teacher/dashboard")
    @PreAuthorize("hasAnyRole('TEACHER', 'ADMIN')")
    public ResponseEntity<List<TeacherHomeworkDTO>> getTeacherDashboard(Principal principal) {
        logger.info("Getting teacher dashboard for user: {}", principal.getName());
        return ResponseEntity.ok(homeworkService.getHomeworksForTeacher(principal));
    }

    @PostMapping("/teacher/grade")
    @PreAuthorize("hasAnyRole('TEACHER', 'ADMIN')")
    public ResponseEntity<com.academix.homeworkservice.dao.entity.Homework> gradeHomework(
            @RequestBody GradeHomeworkRequest request, 
            Principal principal) {
        logger.info("Grading homework: {} by teacher: {}", request.homeworkId(), principal.getName());
        return ResponseEntity.ok(homeworkService.gradeHomework(request, principal));
    }
}
