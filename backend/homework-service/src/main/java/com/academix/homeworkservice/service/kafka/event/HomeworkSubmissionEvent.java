package com.academix.homeworkservice.service.kafka.event;

import java.time.Instant;

public record HomeworkSubmissionEvent(Long homeworkId, Long lessonId, Long studentId, String filePath, Instant timestamp) {}