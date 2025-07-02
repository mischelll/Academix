package com.academix.notificationservice.consumers.events;

public record HomeworkReminderEvent(
    Long homeworkId, 
    Long lessonId, 
    Long studentId, 
    String timeLeft,
    // Student info for notifications
    String studentEmail,
    String studentName,
    String studentPhone
) {}
