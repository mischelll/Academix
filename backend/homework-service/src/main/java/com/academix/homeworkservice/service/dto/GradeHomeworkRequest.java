package com.academix.homeworkservice.service.dto;

import java.math.BigDecimal;

public record GradeHomeworkRequest(
        Long homeworkId,
        BigDecimal grade,
        String comment
) {} 