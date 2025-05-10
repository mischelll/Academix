package com.academix.curriculumservice.web;

import com.academix.curriculumservice.service.SemesterService;
import com.academix.curriculumservice.service.dto.semester.CreateSemesterRequest;
import com.academix.curriculumservice.service.dto.semester.SemesterDTO;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/curriculum")
public class SemesterController {

    private final SemesterService semesterService;

    public SemesterController(SemesterService semesterService) {
        this.semesterService = semesterService;
    }

    @PostMapping("/semesters")
    @PreAuthorize("hasRole('ADMIN')")
    public SemesterDTO createSemester(@RequestBody CreateSemesterRequest request) {
        return semesterService.createSemester(request);
    }

    @GetMapping("/semesters/{id}")
    public SemesterDTO getSemester(@PathVariable Long id) {
        return semesterService.getSemester(id);
    }

    @GetMapping("/semesters")
    public List<SemesterDTO> getAllSemesters() {
        return semesterService.getAllSemesters();
    }

    @GetMapping("/majors/{majorId}/semesters")
    public List<SemesterDTO> getSemestersForMajor(@PathVariable Long majorId) {
        return semesterService.getAllSemestersByMajor(majorId);
    }

    @DeleteMapping("/semesters/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteSemester(@PathVariable Long id) {
        semesterService.delete(id);
    }
}
