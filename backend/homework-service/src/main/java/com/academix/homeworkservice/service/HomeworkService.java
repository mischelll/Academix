package com.academix.homeworkservice.service;

import com.academix.homeworkservice.dao.entity.Homework;
import com.academix.homeworkservice.dao.entity.HomeworkStatus;
import com.academix.homeworkservice.dao.repository.HomeworkRepository;
import com.academix.homeworkservice.service.apiclients.CurriculumClient;
import com.academix.homeworkservice.web.HomeworkController;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class HomeworkService {

    private static Logger logger = LoggerFactory.getLogger(HomeworkService.class);

    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;

    @Value("${cloud.aws.region}")
    private String region;

    private final HomeworkRepository homeworkRepository;
    private final CurriculumClient curriculumClient;

    @Transactional
    public Homework createHomework (HomeworkController.HomeworkDTO homeworkDTO) {
        logger.info("Creating a new homework with key={}, studentId={}", homeworkDTO.fileKey(), homeworkDTO.studentId());
        CurriculumClient.LessonDTO lesson = curriculumClient.getLesson(homeworkDTO.lessonId());
        if(lesson == null) {}
        Homework homework = Homework.builder()
                .credits(2L)
                .deadline(LocalDateTime.now().plusDays(7))
                .description("This is a test homework")
                .endDate(LocalDateTime.now().plusDays(8))
                .title("This is a test homework")
                .filePath(String.format("https://%s.s3.%s.amazonaws.com/%s", bucketName, region, homeworkDTO.fileKey()))
                .studentId(homeworkDTO.studentId())
                .lessonId(1L)
                .status(HomeworkStatus.SUBMITTED)
                .startDate(LocalDateTime.now())
                .submittedDate(LocalDateTime.now())
                .build();

        return homeworkRepository.save(homework);
    }

    @Transactional
    public Page<Homework> getAllHomeworksForStudent(Long studentId) {
        logger.info("Retrieving all homeworks for studentId={}", studentId);
        //TODO: refactor to accept pageable from param
        //PageRequest pageRequest = new PageRequest(0, 20);
        return homeworkRepository.findAllByStudentId(studentId, Pageable.unpaged());
    }
}
