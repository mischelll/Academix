package com.academix.curriculumservice.service;

import com.academix.curriculumservice.dao.entity.CourseStudent;
import com.academix.curriculumservice.dao.repository.CourseStudentRepository;
import com.academix.curriculumservice.dao.repository.LessonRepository;
import com.academix.curriculumservice.dao.entity.Lesson;
import com.academix.curriculumservice.service.dto.course_student.AssignStudentCourseRequest;
import com.academix.curriculumservice.service.dto.course_student.CourseStudentDTO;
import com.academix.curriculumservice.service.mapper.CourseStudentMapper;
import com.academix.curriculumservice.service.apiclient.homework.HomeworkServiceClient;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CourseStudentService {

    private final static Logger LOGGER = LoggerFactory.getLogger(CourseStudentService.class);

    private final CourseStudentRepository repository;
    private final CourseStudentMapper mapper;
    private final LessonRepository lessonRepository;
    private final HomeworkServiceClient homeworkServiceClient;

    public CourseStudentDTO assignStudent(AssignStudentCourseRequest request) {
        CourseStudent courseStudent = mapper.fromCreateRequest(request);
        CourseStudentDTO dto = mapper.toDto(repository.save(courseStudent));

        // Fetch all lessons for the course
        List<Lesson> lessons = lessonRepository.findAllByCourse(courseStudent.getCourse());
        for (Lesson lesson : lessons) {
            // Compose a minimal HomeworkDTO for the homework service
            var homeworkDTO = new java.util.HashMap<String, Object>();
            homeworkDTO.put("title", lesson.getTitle());
            homeworkDTO.put("content", "");
            homeworkDTO.put("filePath", "");
            homeworkDTO.put("studentId", courseStudent.getStudentId());
            homeworkDTO.put("lessonId", lesson.getId());
            homeworkDTO.put("description", lesson.getDescription());
            homeworkDTO.put("credits", 1L);
            // Call homework service
            try {
                homeworkServiceClient.createHomework(homeworkDTO);
            } catch (Exception e) {
                // Log and continue
                System.err.println("Failed to create homework for lesson " + lesson.getId() + ": " + e.getMessage());
            }
        }
        return dto;
    }

    public List<CourseStudentDTO> getAllAssignments() {
        return repository.findAll()
                .stream()
                .map(mapper::toDto)
                .toList();
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }
}
