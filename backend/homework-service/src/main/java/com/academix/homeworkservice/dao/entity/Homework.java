package com.academix.homeworkservice.dao.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "homeworks")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class Homework {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column
    private LocalDateTime startDate;

    @Column
    private LocalDateTime endDate;

    @Column
    private LocalDateTime deadline;

    @Column
    private Long credits;

    @Column
    private Long studentId;

    @Column
    private Long courseId;

    @Column
    private Long lessonId;

    @Column
    private LocalDateTime submittedDate;

    @Column(columnDefinition = "TEXT")
    private String filePath;

    @Enumerated(EnumType.STRING)
    private HomeworkStatus status;

}
