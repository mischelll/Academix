package com.academix.homeworkservice.service;

import com.academix.homeworkservice.dao.entity.Homework;
import com.academix.homeworkservice.dao.entity.HomeworkStatus;
import com.academix.homeworkservice.dao.repository.HomeworkRepository;
import com.academix.homeworkservice.service.apiclients.CurriculumClient;
import com.academix.homeworkservice.service.apiclients.UserServiceClient;
import com.academix.homeworkservice.service.dto.HomeworkMetaDTO;
import com.academix.homeworkservice.service.dto.UserMetaDTO;
import com.academix.homeworkservice.service.kafka.HomeworkEventProducer;
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

        Homework homework = Homework.builder()
                .credits(2L)
                .deadline(LocalDateTime.now().plusDays(7))
                .description("This is a test homework")
                .endDate(LocalDateTime.now().plusDays(8))
                .title("This is a test homework")
                .filePath(homeworkDTO.filePath())
                .studentId(homeworkDTO.studentId())
                .lessonId(1L)
                .status(HomeworkStatus.SUBMITTED)
                .startDate(LocalDateTime.now())
                .submittedDate(LocalDateTime.now())
                .build();

        Homework createdHomework = homeworkRepository.save(homework);
        try {
            HomeworkSubmissionEvent event = new HomeworkSubmissionEvent(
                    createdHomework.getId(),
                    createdHomework.getLessonId(),
                    createdHomework.getStudentId(),
                    homeworkDTO.filePath(),
                    Instant.now()
            );

            JSONObject json = new JSONObject();
            json.put("homeworkId", event.homeworkId());
            json.put("lessonId", event.lessonId());
            json.put("studentId", event.studentId());
            json.put("filePath", event.filePath());
            json.put("submittedAt", event.timestamp().toString());
            homeworkEventProducer.publishHomeworkSubmitted(json);
        } catch (JSONException e) {
            logger.error(e.getMessage());
        }

        return createdHomework;
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
}
