package com.academix.curriculumservice.web;

import com.academix.curriculumservice.dao.entity.Major;
import com.academix.curriculumservice.service.MajorService;
import com.academix.curriculumservice.service.dto.major.CreateMajorRequest;
import com.academix.curriculumservice.service.dto.major.MajorDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/curriculum")
public class CurriculumController {
    private static final Logger logger = LoggerFactory.getLogger(CurriculumController.class);

    private final MajorService majorService;

    public CurriculumController(MajorService majorService) {
        this.majorService = majorService;
    }

    @GetMapping
    public ResponseEntity<?> testController() {
        return ResponseEntity.ok().build();
    }

    @GetMapping("/majors")
    public ResponseEntity<List<MajorDTO>> getMajors() {
        logger.info("getMajors");
        return ResponseEntity.ok(majorService.getAllMajors());
    }

    @PostMapping("/majors")
    public ResponseEntity<MajorDTO> createMajor(@RequestBody CreateMajorRequest createMajorRequest) {
        logger.info("createMajor");
        return ResponseEntity.created(URI.create("/api/curriculum/majors"))
                .body(majorService.create(createMajorRequest));
    }
}
