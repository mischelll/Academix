package com.academix.notificationservice.consumers.events;

import java.time.Instant;

public record HomeworkSubmissionEvent(
    Long homeworkId, 
    Long lessonId, 
    Long studentId, 
    String filePath, 
    Instant timestamp,
    // Student info for notifications
    String studentEmail,
    String studentName,
    String studentPhone,
    // Teacher info for notifications
    String teacherEmail,
    String teacherName
) {}