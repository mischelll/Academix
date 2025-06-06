package com.academix.curriculumservice.web;

import com.academix.curriculumservice.service.MajorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


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
}
