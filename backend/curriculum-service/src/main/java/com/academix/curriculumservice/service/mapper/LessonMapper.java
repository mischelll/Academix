package com.academix.curriculumservice.service.mapper;

import com.academix.curriculumservice.dao.entity.Lesson;
import com.academix.curriculumservice.service.dto.lesson.CreateLessonRequest;
import com.academix.curriculumservice.service.dto.lesson.LessonDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface LessonMapper {

    LessonDTO toDto(Lesson lesson);

    Lesson fromCreateRequest(CreateLessonRequest request);
}
