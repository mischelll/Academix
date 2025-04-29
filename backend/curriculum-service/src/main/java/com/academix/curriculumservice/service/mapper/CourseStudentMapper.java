package com.academix.curriculumservice.service.mapper;

import com.academix.curriculumservice.dao.entity.CourseStudent;
import com.academix.curriculumservice.service.dto.course_student.AssignStudentCourseRequest;
import com.academix.curriculumservice.service.dto.course_student.CourseStudentDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CourseStudentMapper {

    CourseStudentDTO toDto(CourseStudent courseStudent);

    CourseStudent fromCreateRequest(AssignStudentCourseRequest request);
}
