package com.academix.curriculumservice.web;

import com.academix.curriculumservice.dao.entity.Lesson;
import com.academix.curriculumservice.service.LessonService;
import com.academix.curriculumservice.service.dto.lesson.CreateLessonRequest;
import com.academix.curriculumservice.service.dto.lesson.LessonDTO;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/lessons")
public class LessonController {

    private final LessonService lessonService;

    public LessonController(LessonService lessonService) {
        this.lessonService = lessonService;
    }

    @PostMapping
    public LessonDTO createLesson(@RequestBody CreateLessonRequest request) {
        return lessonService.createLesson(request);
    }

    @GetMapping("/{id}")
    public LessonDTO getLesson(@PathVariable Long id) {
        return lessonService.getLesson(id);
    }

    @GetMapping
    public List<LessonDTO> getAllLessons() {
        return lessonService.getAllLessons();
    }

    @DeleteMapping("/{id}")
    public void deleteLesson(@PathVariable Long id) {
        lessonService.delete(id);
    }
}
