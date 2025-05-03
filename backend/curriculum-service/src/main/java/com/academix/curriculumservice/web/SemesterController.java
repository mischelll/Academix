package com.academix.curriculumservice.web;

import com.academix.curriculumservice.dao.entity.Semester;
import com.academix.curriculumservice.service.SemesterService;
import com.academix.curriculumservice.service.dto.semester.CreateSemesterRequest;
import com.academix.curriculumservice.service.dto.semester.SemesterDTO;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/curriculum/semesters")
public class SemesterController {

    private final SemesterService semesterService;

    public SemesterController(SemesterService semesterService) {
        this.semesterService = semesterService;
    }

    @PostMapping
    public SemesterDTO createSemester(@RequestBody CreateSemesterRequest request) {
        return semesterService.createSemester(request);
    }

    @GetMapping("/{id}")
    public SemesterDTO getSemester(@PathVariable Long id) {
        return semesterService.getSemester(id);
    }

    @GetMapping
    public List<SemesterDTO> getAllSemesters() {
        return semesterService.getAllSemesters();
    }

    @DeleteMapping("/{id}")
    public void deleteSemester(@PathVariable Long id) {
        semesterService.delete(id);
    }
}
