package com.academix.curriculumservice.web;

import com.academix.curriculumservice.service.HomeworkBackfillService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/curriculum")
public class HomeworkBackfillController {
    private final HomeworkBackfillService homeworkBackfillService;

    public HomeworkBackfillController(HomeworkBackfillService homeworkBackfillService) {
        this.homeworkBackfillService = homeworkBackfillService;
    }

    @PostMapping("/homeworks/backfill-for-student/{studentId}")
    public ResponseEntity<Void> backfillHomeworksForStudent(@PathVariable Long studentId) {
        homeworkBackfillService.backfillHomeworksForStudent(studentId);
        return ResponseEntity.ok().build();
    }
} 