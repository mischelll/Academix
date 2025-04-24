package com.academix.homeworkservice.web;

import com.academix.homeworkservice.service.CloudStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/files")
public class FileUploadController {

    private final CloudStorageService cloudStorageService;

    public FileUploadController(CloudStorageService cloudStorageService) {
        this.cloudStorageService = cloudStorageService;
    }

    @GetMapping("/presigned-url")
    public ResponseEntity<String> generatePresignedUrl(@RequestParam String fileName) {
        return ResponseEntity
                .status(HttpStatusCode.valueOf(201))
                .body(cloudStorageService.generatePresignedUrl(fileName));
    }
}
