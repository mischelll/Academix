package com.academix.homeworkservice.service.kafka.event;

public record HomeworkReminderEvent(Long homeworkId, Long lessonId, Long studentId, String timeLeft) {}
