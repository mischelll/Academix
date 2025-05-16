package com.academix.curriculumservice.service.apiclient.homework;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "homework-service", url = "${services.homework.url}")
public class HomeworkServiceClient {
}
