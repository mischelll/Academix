package com.academix.curriculumservice.web;

import com.academix.curriculumservice.service.LessonService;
import com.academix.curriculumservice.service.dto.lesson.CreateLessonRequest;
import com.academix.curriculumservice.service.dto.lesson.LessonDTO;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/curriculum")
public class LessonController {

    private final LessonService lessonService;

    public LessonController(LessonService lessonService) {
        this.lessonService = lessonService;
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    public LessonDTO createLesson(@RequestBody CreateLessonRequest request) {
        return lessonService.createLesson(request);
    }

    @GetMapping("/lessons/{id}")
    public LessonDTO getLesson(@PathVariable Long id) {
        return lessonService.getLesson(id);
    }

    @GetMapping("/lessons")
    public List<LessonDTO> getAllLessons() {
        return lessonService.getAllLessons();
    }

    @GetMapping("/courses/{courseId}/lessons")
    public List<LessonDTO> getLessonsByCourse(@PathVariable Long courseId) {
        return lessonService.getAllLessonsByCourse(courseId);
    }

    @DeleteMapping("/lessons/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    public void deleteLesson(@PathVariable Long id) {
        lessonService.delete(id);
    }
}
