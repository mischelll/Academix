package com.academix.curriculumservice.service;

import com.academix.curriculumservice.dao.entity.CourseStudent;
import com.academix.curriculumservice.dao.entity.Lesson;
import com.academix.curriculumservice.dao.repository.CourseStudentRepository;
import com.academix.curriculumservice.dao.repository.LessonRepository;
import com.academix.curriculumservice.service.apiclient.homework.HomeworkServiceClient;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class HomeworkBackfillService {
    private final CourseStudentRepository courseStudentRepository;
    private final LessonRepository lessonRepository;
    private final HomeworkServiceClient homeworkServiceClient;

    public HomeworkBackfillService(
        CourseStudentRepository courseStudentRepository,
        LessonRepository lessonRepository,
        HomeworkServiceClient homeworkServiceClient
    ) {
        this.courseStudentRepository = courseStudentRepository;
        this.lessonRepository = lessonRepository;
        this.homeworkServiceClient = homeworkServiceClient;
    }

    public void backfillHomeworksForStudent(Long studentId) {
        List<CourseStudent> enrollments = courseStudentRepository.findByStudentId(studentId);
        for (CourseStudent cs : enrollments) {
            List<Lesson> lessons = lessonRepository.findAllByCourse(cs.getCourse());
            for (Lesson lesson : lessons) {
                // Check if homework exists for this student and lesson
                boolean exists = false;
                try {
                    var meta = homeworkServiceClient.getHomeworkMetaByLessonId(lesson.getId());
                    if (meta != null && meta.studentId().equals(studentId)) {
                        exists = true;
                    }
                } catch (Exception e) {
                    // Assume not found, so create
                }
                
                // Only create if homework doesn't exist
                if (!exists) {
                    var homeworkDTO = new java.util.HashMap<String, Object>();
                    homeworkDTO.put("title", lesson.getTitle());
                    homeworkDTO.put("content", "");
                    homeworkDTO.put("filePath", "");
                    homeworkDTO.put("studentId", studentId);
                    homeworkDTO.put("lessonId", lesson.getId());
                    homeworkDTO.put("description", lesson.getDescription());
                    homeworkDTO.put("credits", 1L);
                    try {
                        homeworkServiceClient.createHomework(homeworkDTO);
                    } catch (Exception ex) {
                        System.err.println("Failed to create homework for lesson " + lesson.getId() + ": " + ex.getMessage());
                    }
                }
            }
        }
    }
} 