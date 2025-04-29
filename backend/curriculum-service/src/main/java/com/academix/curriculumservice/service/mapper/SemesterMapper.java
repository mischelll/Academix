package com.academix.curriculumservice.service.mapper;

import com.academix.curriculumservice.dao.entity.Semester;
import com.academix.curriculumservice.service.dto.semester.CreateSemesterRequest;
import com.academix.curriculumservice.service.dto.semester.SemesterDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SemesterMapper {

    SemesterDTO toDto(Semester semester);

    Semester fromCreateRequest(CreateSemesterRequest request);
}
