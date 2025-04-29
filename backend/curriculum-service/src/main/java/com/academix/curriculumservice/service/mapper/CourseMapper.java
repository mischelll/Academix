package com.academix.curriculumservice.service.mapper;

import com.academix.curriculumservice.dao.entity.Course;
import com.academix.curriculumservice.service.dto.course.CourseDTO;
import com.academix.curriculumservice.service.dto.course.CreateCourseRequest;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface CourseMapper {

    CourseMapper INSTANCE = Mappers.getMapper(CourseMapper.class);

    CourseDTO toDto(Course course);

    Course fromCreateRequest(CreateCourseRequest request);
}
