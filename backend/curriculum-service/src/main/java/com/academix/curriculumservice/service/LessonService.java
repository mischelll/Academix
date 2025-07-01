package com.academix.curriculumservice.service;

import com.academix.curriculumservice.dao.entity.Course;
import com.academix.curriculumservice.dao.entity.Lesson;
import com.academix.curriculumservice.dao.entity.Major;
import com.academix.curriculumservice.dao.repository.CourseRepository;
import com.academix.curriculumservice.dao.repository.LessonRepository;
import com.academix.curriculumservice.service.apiclient.homework.HomeworkMetaDTO;
import com.academix.curriculumservice.service.apiclient.homework.HomeworkServiceClient;
import com.academix.curriculumservice.service.dto.lesson.CreateLessonRequest;
import com.academix.curriculumservice.service.dto.lesson.LessonDTO;
import com.academix.curriculumservice.service.dto.semester.SemesterDTO;
import com.academix.curriculumservice.service.mapper.LessonMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LessonService {

    private final LessonRepository lessonRepository;
    private final CourseRepository courseRepository;
    private final HomeworkServiceClient homeworkServiceClient;
    private final LessonMapper mapper;

    public LessonDTO createLesson(CreateLessonRequest request) {
        Lesson lesson = mapper.fromCreateRequest(request);
        return mapper.toDto(lessonRepository.save(lesson));
    }

    public LessonDTO getLesson(Long id) {
        HomeworkMetaDTO homeworkMetaByLessonId = homeworkServiceClient.getHomeworkMetaByLessonId(id);
        LessonDTO lesson = lessonRepository.findById(id)
                .map(mapper::toDto)
                .orElseThrow(() -> new RuntimeException("Lesson not found"));

        return new LessonDTO(lesson.id(), lesson.title(), lesson.description(), lesson.courseId(), homeworkMetaByLessonId.endTime());
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
        List<Lesson> allByCourse = lessonRepository.findAllByCourse(course);

        if (allByCourse.isEmpty()) return new ArrayList<>();
        Set<HomeworkMetaDTO> homeworkMetaByLessonIds = homeworkServiceClient
                .getHomeworkMetaByLessonIds(allByCourse
                        .stream()
                        .map(Lesson::getId)
                        .collect(Collectors.toSet()));

        Map<Long, Long> lessonEndTimeMap = homeworkMetaByLessonIds.stream().distinct()
                .collect(Collectors.toMap(
                        HomeworkMetaDTO::lessonId,
                        HomeworkMetaDTO::endTime,
                        (a, b) -> Math.max(a, b)
                ));

        return allByCourse.stream()
                .map(lesson -> {
                    LessonDTO dto = mapper.toDto(lesson);
                    Long endTime = lessonEndTimeMap.get(lesson.getId());
                    return new LessonDTO(
                            dto.id(),
                            dto.title(),
                            dto.description(),
                            dto.courseId(),
                            endTime
                    );
                })
                .toList();
    }
}
