package com.academix.curriculumservice.service.mapper;

import com.academix.curriculumservice.dao.entity.CourseTeacher;
import com.academix.curriculumservice.service.dto.course_teacher.AssignTeacherCourseRequest;
import com.academix.curriculumservice.service.dto.course_teacher.CourseTeacherDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CourseTeacherMapper {

    CourseTeacherDTO toDto(CourseTeacher courseTeacher);

    CourseTeacher fromCreateRequest(AssignTeacherCourseRequest request);
}
