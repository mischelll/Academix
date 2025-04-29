package com.academix.curriculumservice.dao.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "semesters")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Semester {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String name;

    @ManyToOne(targetEntity = Major.class)
    @JoinColumn(name = "major_id")
    private Major major;

    @OneToMany(mappedBy = "semester", cascade = CascadeType.ALL)
    private List<Course> courses = new ArrayList<>();
}
