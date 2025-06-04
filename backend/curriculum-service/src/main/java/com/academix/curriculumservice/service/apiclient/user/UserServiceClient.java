package com.academix.curriculumservice.service.apiclient.user;

import com.academix.curriculumservice.config.FeignConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "user-service", url = "${services.user.url}", configuration = FeignConfig.class)
public interface UserServiceClient {

    @GetMapping("/users/internal/{userId}")
    UserMetaDTO getUserById(Long userId);
}
