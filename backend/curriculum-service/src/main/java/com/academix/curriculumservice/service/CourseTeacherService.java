package com.academix.curriculumservice.service;

import com.academix.curriculumservice.dao.entity.Course;
import com.academix.curriculumservice.dao.entity.CourseTeacher;
import com.academix.curriculumservice.dao.entity.Lesson;
import com.academix.curriculumservice.dao.repository.CourseTeacherRepository;
import com.academix.curriculumservice.dao.repository.LessonRepository;
import com.academix.curriculumservice.service.apiclient.user.UserMetaDTO;
import com.academix.curriculumservice.service.apiclient.user.UserServiceClient;
import com.academix.curriculumservice.service.dto.course_teacher.AssignTeacherCourseRequest;
import com.academix.curriculumservice.service.dto.course_teacher.CourseTeacherDTO;
import com.academix.curriculumservice.service.mapper.CourseTeacherMapper;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CourseTeacherService {

    private final static Logger LOGGER = LoggerFactory.getLogger(CourseTeacherService.class);

    private final CourseTeacherRepository repository;
    private final LessonRepository lessonRepository;
    private final CourseTeacherMapper mapper;
    private final UserServiceClient userServiceClient;

    public CourseTeacherDTO assignTeacher(AssignTeacherCourseRequest request) {
        CourseTeacher courseTeacher = mapper.fromCreateRequest(request);
        return mapper.toDto(repository.save(courseTeacher));
    }

    public List<CourseTeacherDTO> getAllAssignments() {
        return repository.findAll()
                .stream()
                .map(mapper::toDto)
                .toList();
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }

    public CourseTeacherDTO findTeacherByCourse(Long courseId) {
        CourseTeacherDTO courseTeacherDTO = repository.findByCourseId(courseId)
                .stream()
                .map(mapper::toDto)
                .toList().getFirst();
        UserMetaDTO userById = userServiceClient.getUserById(courseTeacherDTO.teacherId());


        return new CourseTeacherDTO(courseTeacherDTO.id(), courseId, userById.id(),userById.name(), userById.email());

    }

    public boolean isTeacherForLesson(Long userId, Long lessonId) {
        Lesson lesson = lessonRepository.findById(lessonId)
                .orElseThrow(() -> new RuntimeException("Lesson not found"));
        Course course = lesson.getCourse();
        return repository.existsByCourseAndTeacherId(course, userId);
    }

    public List<Long> getTeacherLessonIds(Long teacherId) {
        // Get all courses where the teacher is assigned
        List<Course> teacherCourses = repository.findByTeacherId(teacherId)
                .stream()
                .map(CourseTeacher::getCourse)
                .toList();

        // Get all lessons from those courses
        return teacherCourses.stream()
                .flatMap(course -> lessonRepository.findAllByCourse(course).stream())
                .map(Lesson::getId)
                .collect(Collectors.toList());
    }
}

