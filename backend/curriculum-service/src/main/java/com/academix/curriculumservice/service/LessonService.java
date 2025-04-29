package com.academix.curriculumservice.service;

import com.academix.curriculumservice.dao.entity.Lesson;
import com.academix.curriculumservice.dao.repository.LessonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LessonService {
    private final LessonRepository lessonRepository;

    public List<Lesson> findAll() {
        return lessonRepository.findAll();
    }

    public Lesson findById(Long id) {
        return lessonRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Lesson not found"));
    }

    public Lesson create(Lesson lesson) {
        return lessonRepository.save(lesson);
    }

    public Lesson update(Long id, Lesson updatedLesson) {
        Lesson existing = findById(id);
        existing.setTitle(updatedLesson.getTitle());
        existing.setCourse(updatedLesson.getCourse());
        return lessonRepository.save(existing);
    }

    public void delete(Long id) {
        lessonRepository.deleteById(id);
    }
}
