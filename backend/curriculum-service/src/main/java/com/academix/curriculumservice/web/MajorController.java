package com.academix.curriculumservice.web;

import com.academix.curriculumservice.service.MajorService;
import com.academix.curriculumservice.service.dto.major.CreateMajorRequest;
import com.academix.curriculumservice.service.dto.major.MajorDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/majors")
@RequiredArgsConstructor
public class MajorController {

    private final MajorService service;

    @PostMapping
    public MajorDTO createMajor(@RequestBody CreateMajorRequest request) {
        return service.create(request);
    }

    @GetMapping("/{id}")
    public MajorDTO getMajor(@PathVariable Long id) {
        return service.getMajor(id);
    }

    @GetMapping
    public List<MajorDTO> getAllMajors() {
        return service.getAllMajors();
    }

    @DeleteMapping("/{id}")
    public void deleteMajor(@PathVariable Long id) {
        service.delete(id);
    }
}
