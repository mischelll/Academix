package com.academix.curriculumservice.service;

import com.academix.curriculumservice.dao.entity.Semester;
import com.academix.curriculumservice.dao.repository.SemesterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SemesterService {
    private final SemesterRepository semesterRepository;

    public List<Semester> findAll() {
        return semesterRepository.findAll();
    }

    public Semester findById(Long id) {
        return semesterRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Semester not found"));
    }

    public Semester create(Semester semester) {
        return semesterRepository.save(semester);
    }

    public Semester update(Long id, Semester updatedSemester) {
        Semester existing = findById(id);
        existing.setName(updatedSemester.getName());
        existing.setMajor(updatedSemester.getMajor());
        return semesterRepository.save(existing);
    }

    public void delete(Long id) {
        semesterRepository.deleteById(id);
    }
}
