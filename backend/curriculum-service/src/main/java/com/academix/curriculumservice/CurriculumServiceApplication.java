package com.academix.curriculumservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class CurriculumServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(CurriculumServiceApplication.class, args);
    }

}
