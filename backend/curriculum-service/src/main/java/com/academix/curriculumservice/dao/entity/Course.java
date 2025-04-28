package com.academix.curriculumservice.dao.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "courses")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String name;

    @Column
    private String description;

    @Column
    private Long teacherId;

    @ManyToOne(targetEntity = Semester.class)
    private Semester semester;

    @OneToMany(mappedBy = "course", orphanRemoval = true)
    private List<Lesson> lessons;
}
