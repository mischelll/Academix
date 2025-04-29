package com.academix.curriculumservice.web;

import com.academix.curriculumservice.dao.entity.Semester;
import com.academix.curriculumservice.service.SemesterService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/semesters")
public class SemesterController {

    private final SemesterService semesterService;

    public SemesterController(SemesterService semesterService) {
        this.semesterService = semesterService;
    }

    @GetMapping
    public List<Semester> getAllSemesters() {
        return semesterService.findAll();
    }

    @GetMapping("/{id}")
    public Semester getSemester(@PathVariable Long id) {
        return semesterService.findById(id);
    }

    @PostMapping
    public Semester createSemester(@RequestBody Semester semester) {
        return semesterService.create(semester);
    }

    @PutMapping("/{id}")
    public Semester updateSemester(@PathVariable Long id, @RequestBody Semester semester) {
        return semesterService.update(id, semester);
    }

    @DeleteMapping("/{id}")
    public void deleteSemester(@PathVariable Long id) {
        semesterService.delete(id);
    }
}
