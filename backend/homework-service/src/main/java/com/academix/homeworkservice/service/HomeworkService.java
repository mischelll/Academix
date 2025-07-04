package com.academix.homeworkservice.service;

import com.academix.homeworkservice.dao.entity.Homework;
import com.academix.homeworkservice.dao.entity.HomeworkStatus;
import com.academix.homeworkservice.dao.repository.HomeworkRepository;
import com.academix.homeworkservice.service.apiclients.CurriculumClient;
import com.academix.homeworkservice.service.apiclients.UserServiceClient;
import com.academix.homeworkservice.service.dto.GradeHomeworkRequest;
import com.academix.homeworkservice.service.dto.HomeworkMetaDTO;
import com.academix.homeworkservice.service.dto.TeacherHomeworkDTO;
import com.academix.homeworkservice.service.dto.UserMetaDTO;
import com.academix.homeworkservice.service.kafka.HomeworkEventProducer;
import com.academix.homeworkservice.service.kafka.event.HomeworkReviewedEvent;
import com.academix.homeworkservice.service.kafka.event.HomeworkSubmissionEvent;
import com.academix.homeworkservice.web.HomeworkController;
import lombok.RequiredArgsConstructor;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class HomeworkService {

    private final static Logger logger = LoggerFactory.getLogger(HomeworkService.class);

    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;

    @Value("${cloud.aws.region}")
    private String region;

    private final HomeworkRepository homeworkRepository;
    private final UserServiceClient userServiceClient;
    private final CurriculumClient curriculumClient;
    private final CloudStorageService cloudStorageService;
    private final HomeworkEventProducer homeworkEventProducer;

    @Transactional
    public Homework createHomework(HomeworkController.HomeworkDTO homeworkDTO) {
        logger.info("Creating a new homework with key={}, studentId={}", homeworkDTO.filePath(), homeworkDTO.studentId());

        boolean isFileUpload = homeworkDTO.filePath() != null && !homeworkDTO.filePath().isEmpty();

        Optional<Homework> existingOpt = homeworkRepository.findByLessonIdAndStudentId(homeworkDTO.lessonId(), homeworkDTO.studentId());
        if (existingOpt.isPresent()) {
            Homework existing = existingOpt.get();
            if (isFileUpload) {
                // Update the existing homework for file upload
                existing.setFilePath(homeworkDTO.filePath());
                existing.setStatus(HomeworkStatus.SUBMITTED);
                existing.setSubmittedDate(LocalDateTime.now());
                existing.setTitle(homeworkDTO.title());
                existing.setDescription(homeworkDTO.description());
                existing.setCredits(homeworkDTO.credits());
                existing.setEndDate(LocalDateTime.now().plusDays(8));
                existing.setDeadline(LocalDateTime.now().plusDays(7));
                Homework updated = homeworkRepository.save(existing);
                sendNotificationEvent(homeworkDTO, updated);
                return updated;
            } else {
                // For backfill or any non-upload, just return the existing homework as-is
                return existing;
            }
        }

        // Create new homework (for backfill or new uploads)
        Homework.HomeworkBuilder builder = Homework.builder()
                .credits(homeworkDTO.credits())
                .deadline(LocalDateTime.now().plusDays(7))
                .description(homeworkDTO.description())
                .endDate(LocalDateTime.now().plusDays(8))
                .title(homeworkDTO.title())
                .filePath(homeworkDTO.filePath())
                .studentId(homeworkDTO.studentId())
                .lessonId(homeworkDTO.lessonId())
                .startDate(LocalDateTime.now());

        if (isFileUpload) {
            builder.status(HomeworkStatus.SUBMITTED);
            builder.submittedDate(LocalDateTime.now());
        } else {
            builder.status(HomeworkStatus.OPEN);
        }

        Homework homework = builder.build();
        Homework createdHomework = homeworkRepository.save(homework);
        if (isFileUpload) {
            sendNotificationEvent(homeworkDTO, createdHomework);
        }
        return createdHomework;
    }

    private void sendNotificationEvent(HomeworkController.HomeworkDTO homeworkDTO, Homework createdHomework) {
        try {
            // Fetch student information
            UserMetaDTO student = userServiceClient.getUserById(createdHomework.getStudentId());
            
            // Fetch teacher information for the lesson
            String teacherEmail = null;
            String teacherName = null;
            try {
                // Get teacher info from curriculum service
                var teacherInfo = curriculumClient.getTeacherByLessonId(createdHomework.getLessonId());
                if (teacherInfo != null) {
                    teacherEmail = teacherInfo.teacherEmail();
                    teacherName = teacherInfo.teacherName();
                }
            } catch (Exception e) {
                logger.warn("Could not fetch teacher info for lesson {}: {}", createdHomework.getLessonId(), e.getMessage());
            }

            HomeworkSubmissionEvent event = new HomeworkSubmissionEvent(
                    createdHomework.getId(),
                    createdHomework.getLessonId(),
                    createdHomework.getStudentId(),
                    homeworkDTO.filePath(),
                    Instant.now(),
                    student != null ? student.email() : null,
                    student != null ? student.name() : null,
                    student != null ? student.phone() : null,
                    teacherEmail,
                    teacherName
            );

            JSONObject json = new JSONObject();
            json.put("homeworkId", event.homeworkId());
            json.put("lessonId", event.lessonId());
            json.put("studentId", event.studentId());
            json.put("filePath", event.filePath());
            json.put("submittedAt", event.timestamp().toString());
            json.put("studentEmail", event.studentEmail());
            json.put("studentName", event.studentName());
            json.put("studentPhone", event.studentPhone());
            json.put("teacherEmail", event.teacherEmail());
            json.put("teacherName", event.teacherName());
            homeworkEventProducer.publishHomeworkSubmitted(json);
        } catch (JSONException e) {
            logger.error(e.getMessage());
        }
    }

    @Transactional(readOnly = true)
    public Page<Homework> getAllHomeworksForStudent(Long studentId) {
        logger.info("Retrieving all homeworks for studentId={}", studentId);
        //TODO: refactor to accept pageable from param
        //PageRequest pageRequest = new PageRequest(0, 20);
        return homeworkRepository.findAllByStudentId(studentId, Pageable.unpaged());
    }

    @Transactional(readOnly = true)
    public HomeworkMetaDTO getHomeworkByLessonId(Long lessonId) {
        Homework byLessonId = homeworkRepository.findByLessonIdEquals(lessonId);

        return new HomeworkMetaDTO(
                byLessonId.getId(),
                byLessonId.getLessonId(),
                byLessonId.getStudentId(),
                byLessonId.getEndDate().toInstant(ZoneOffset.UTC).toEpochMilli()
        );
    }

    @Transactional(readOnly = true)
    public List<HomeworkMetaDTO> getHomeworkByLessonIds(List<Long> lessonIds) {
        return homeworkRepository.findByLessonIdIn(lessonIds).stream().map(hw -> new HomeworkMetaDTO(
                hw.getId(),
                hw.getLessonId(),
                hw.getStudentId(),
                hw.getEndDate().toInstant(ZoneOffset.UTC).toEpochMilli()
        )).toList();
    }

    public String getDownloadUrl(Long lessonId, Principal principal) {
        Homework homework = homeworkRepository.findByLessonIdEquals(lessonId);
        if (homework == null) {
            throw new RuntimeException("Homework not found");
        }

        UserMetaDTO user = userServiceClient.getUserByEmail(principal.getName());

        boolean isUploader = homework.getStudentId().equals(user.id());
        boolean isTeacher = curriculumClient.isTeacherOfCourse(user.id(), homework.getLessonId());
        boolean isAdmin = user.roles().contains("ROLE_ADMIN");

        if (!isUploader && !isTeacher && !isAdmin) {
            throw new AccessDeniedException("You are not allowed to access this file.");
        }

        return cloudStorageService.generatePresignedGetUrl(homework.getFilePath());
    }

    @Transactional(readOnly = true)
    public List<TeacherHomeworkDTO> getHomeworksForTeacher(Principal principal) {
        UserMetaDTO teacher = userServiceClient.getUserByEmail(principal.getName());
        List<Long> teacherLessonIds = curriculumClient.getTeacherLessonIds(teacher.id());
        
        if (teacherLessonIds.isEmpty()) {
            return List.of();
        }

        List<Homework> teacherHomeworks = homeworkRepository.findByLessonIdIn(teacherLessonIds);

        return teacherHomeworks.stream()
                .map(homework -> {
                    UserMetaDTO student = userServiceClient.getUserById(homework.getStudentId());

                    String lessonTitle = "Lesson " + homework.getLessonId();
                    try {
                        CurriculumClient.LessonInfo lessonInfo = curriculumClient.getLessonById(homework.getLessonId());
                        lessonTitle = lessonInfo.title();
                    } catch (Exception e) {
                        logger.warn("Could not fetch lesson title for lessonId: {}", homework.getLessonId(), e);
                    }
                    
                    return new TeacherHomeworkDTO(
                            homework.getId(),
                            homework.getTitle(),
                            homework.getDescription(),
                            homework.getStudentId(),
                            student.name(),
                            student.email(),
                            homework.getLessonId(),
                            lessonTitle,
                            homework.getFilePath(),
                            homework.getSubmittedDate(),
                            homework.getDeadline(),
                            homework.getStatus().toString(),
                            homework.getGrade(),
                            homework.getComment()
                    );
                })
                .toList();
    }

    @Transactional
    public Homework gradeHomework(GradeHomeworkRequest request, Principal principal) {
        UserMetaDTO teacher = userServiceClient.getUserByEmail(principal.getName());
        
        Homework homework = homeworkRepository.findById(request.homeworkId())
                .orElseThrow(() -> new RuntimeException("Homework not found"));

        if (!curriculumClient.isTeacherOfCourse(teacher.id(), homework.getLessonId())) {
            throw new AccessDeniedException("You are not authorized to grade this homework");
        }

        homework.setGrade(request.grade());
        homework.setComment(request.comment());
        homework.setStatus(HomeworkStatus.REVIEWED);

        Homework gradedHomework = homeworkRepository.save(homework);
        
        // Send notification event
        sendGradingNotificationEvent(gradedHomework, teacher.id());

        return gradedHomework;
    }

    private void sendGradingNotificationEvent(Homework homework, Long teacherId) {
        try {
            // Fetch student information
            UserMetaDTO student = userServiceClient.getUserById(homework.getStudentId());
            
            HomeworkReviewedEvent event = new HomeworkReviewedEvent(
                    homework.getId(),
                    homework.getLessonId(),
                    homework.getGrade() != null ? homework.getGrade().longValue() : null,
                    homework.getStudentId(),
                    teacherId,
                    Instant.now(),
                    student != null ? student.email() : null,
                    student != null ? student.name() : null,
                    student != null ? student.phone() : null
            );

            JSONObject json = new JSONObject();
            json.put("homeworkId", event.homeworkId());
            json.put("lessonId", event.lessonId());
            json.put("grade", event.grade());
            json.put("studentId", event.studentId());
            json.put("reviewedBy", event.reviewedBy());
            json.put("timestamp", event.timestamp().toString());
            json.put("studentEmail", event.studentEmail());
            json.put("studentName", event.studentName());
            json.put("studentPhone", event.studentPhone());
            homeworkEventProducer.publishHomeworkReviewed(json);
        } catch (JSONException e) {
            logger.error(e.getMessage());
        }
    }
}
