package com.academix.homeworkservice.service.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record TeacherHomeworkDTO(
        Long id,
        String title,
        String description,
        Long studentId,
        String studentName,
        String studentEmail,
        Long lessonId,
        String lessonTitle,
        String filePath,
        LocalDateTime submittedDate,
        LocalDateTime deadline,
        String status,
        BigDecimal grade,
        String comment
) {} 