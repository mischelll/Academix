package com.academix.notificationservice.consumers.events;

import java.time.Instant;

public record HomeworkReviewedEvent(Long homeworkId, Long lessonId, Long grade, Long studentId, Long reviewedBy, Instant timestamp) {}
