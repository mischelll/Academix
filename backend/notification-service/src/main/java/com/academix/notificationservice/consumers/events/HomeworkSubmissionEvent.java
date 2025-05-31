package com.academix.notificationservice.consumers.events;

import java.time.Instant;

public record HomeworkSubmissionEvent(Long homeworkId, Long lessonId, Long studentId, String filePath, Instant timestamp) {}