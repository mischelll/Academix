package com.academix.curriculumservice.service;

import com.academix.curriculumservice.dao.entity.Major;
import com.academix.curriculumservice.dao.repository.MajorRepository;
import com.academix.curriculumservice.service.dto.major.CreateMajorRequest;
import com.academix.curriculumservice.service.dto.major.MajorDTO;
import com.academix.curriculumservice.service.mapper.MajorMapper;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MajorService {

    private static Logger logger = LoggerFactory.getLogger(MajorService.class);

    private final MajorRepository majorRepository;
    private final SemesterService semesterService;
    private final MajorMapper mapper;

    public MajorDTO create(CreateMajorRequest request) {
        Major major = mapper.fromCreateRequest(request);
        return mapper.toDto(majorRepository.save(major));
    }

    public MajorDTO getMajor(Long id) {
        return majorRepository.findById(id)
                .map(mapper::toDto)
                .orElseThrow(() -> new RuntimeException("Major not found"));
    }

    public List<MajorDTO> getAllMajors() {
        return majorRepository.findAll()
                .stream()
                .map(mapper::toDto)
                .toList();
    }

    public List<MajorDTO> getAllMajorSemesters(Long majorId) {
        return majorRepository.findAll()
                .stream()
                .map(mapper::toDto)
                .toList();
    }

    public void delete(Long id) {
        majorRepository.deleteById(id);
    }
}
