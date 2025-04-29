package com.academix.curriculumservice.service;

import com.academix.curriculumservice.dao.entity.Semester;
import com.academix.curriculumservice.dao.repository.SemesterRepository;
import com.academix.curriculumservice.service.dto.semester.CreateSemesterRequest;
import com.academix.curriculumservice.service.dto.semester.SemesterDTO;
import com.academix.curriculumservice.service.mapper.SemesterMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SemesterService {

    private final SemesterRepository semesterRepository;
    private final SemesterMapper mapper;

    public SemesterDTO createSemester(CreateSemesterRequest request) {
        Semester semester = mapper.fromCreateRequest(request);
        return mapper.toDto(semesterRepository.save(semester));
    }

    public SemesterDTO getSemester(Long id) {
        return semesterRepository.findById(id)
                .map(mapper::toDto)
                .orElseThrow(() -> new RuntimeException("Semester not found"));
    }

    public List<SemesterDTO> getAllSemesters() {
        return semesterRepository.findAll()
                .stream()
                .map(mapper::toDto)
                .toList();
    }

    public void delete(Long id) {
        semesterRepository.deleteById(id);
    }
}
