package com.academix.homeworkservice.web;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/homeworks")
public class HomeworkController {

    record Homework(String title, String content) {}

    @GetMapping("/current")
    public ResponseEntity<Homework> getHomeworks() {
        return ResponseEntity.ok(new Homework("Math III", "f(x) = 0"));
    }
}
