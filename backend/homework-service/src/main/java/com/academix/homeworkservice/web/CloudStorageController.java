package com.academix.homeworkservice.web;

import com.academix.homeworkservice.service.CloudStorageService;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/files")
public class CloudStorageController {

    private final CloudStorageService cloudStorageService;

    public CloudStorageController(CloudStorageService cloudStorageService) {
        this.cloudStorageService = cloudStorageService;
    }

    @GetMapping("/presigned-url")
    public ResponseEntity<String> generatePresignedUrl(@RequestParam String fileName) {
        return ResponseEntity
                .status(HttpStatusCode.valueOf(201))
                .body(cloudStorageService.generatePresignedPutUrl(fileName));
    }
}
