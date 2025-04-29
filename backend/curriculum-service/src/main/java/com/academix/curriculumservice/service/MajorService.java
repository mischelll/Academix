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

    public List<Major> getAllMajors(){
        logger.info("Getting all majors");
        return majorRepository.findAll();
    }
}
