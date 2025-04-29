package com.academix.curriculumservice.service;

import com.academix.curriculumservice.dao.entity.Major;
import com.academix.curriculumservice.dao.repository.MajorRepository;
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

    public List<Major> findAll() {
        return majorRepository.findAll();
    }

    public Major findById(Long id) {
        return majorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Major not found"));
    }

    public Major create(Major major) {
        return majorRepository.save(major);
    }

    public Major update(Long id, Major updatedMajor) {
        Major existing = findById(id);
        existing.setName(updatedMajor.getName());
        return majorRepository.save(existing);
    }

    public void delete(Long id) {
        majorRepository.deleteById(id);
    }
}
