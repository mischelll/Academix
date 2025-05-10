package com.academix.curriculumservice.service;

import com.academix.curriculumservice.dao.entity.Course;
import com.academix.curriculumservice.dao.entity.Lesson;
import com.academix.curriculumservice.dao.entity.Major;
import com.academix.curriculumservice.dao.repository.CourseRepository;
import com.academix.curriculumservice.dao.repository.LessonRepository;
import com.academix.curriculumservice.service.dto.lesson.CreateLessonRequest;
import com.academix.curriculumservice.service.dto.lesson.LessonDTO;
import com.academix.curriculumservice.service.dto.semester.SemesterDTO;
import com.academix.curriculumservice.service.mapper.LessonMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LessonService {

    private final LessonRepository lessonRepository;
    private final CourseRepository courseRepository;
    private final LessonMapper mapper;

    public LessonDTO createLesson(CreateLessonRequest request) {
        Lesson lesson = mapper.fromCreateRequest(request);
        return mapper.toDto(lessonRepository.save(lesson));
    }

    public LessonDTO getLesson(Long id) {
        return lessonRepository.findById(id)
                .map(mapper::toDto)
                .orElseThrow(() -> new RuntimeException("Lesson not found"));
    }

    public List<LessonDTO> getAllLessons() {
        return lessonRepository.findAll()
                .stream()
                .map(mapper::toDto)
                .toList();
    }

    public void delete(Long id) {
        lessonRepository.deleteById(id);
    }

    public List<LessonDTO> getAllLessonsByCourse(Long courseId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found"));

        return lessonRepository.findAllByCourse(course)
                .stream()
                .map(mapper::toDto)
                .toList();
    }
}
