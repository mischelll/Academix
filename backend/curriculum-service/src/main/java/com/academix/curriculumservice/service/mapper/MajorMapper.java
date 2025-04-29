package com.academix.curriculumservice.service.mapper;

import com.academix.curriculumservice.dao.entity.Major;
import com.academix.curriculumservice.service.dto.major.CreateMajorRequest;
import com.academix.curriculumservice.service.dto.major.MajorDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MajorMapper {

    MajorDTO toDto(Major major);

    Major fromCreateRequest(CreateMajorRequest request);
}
