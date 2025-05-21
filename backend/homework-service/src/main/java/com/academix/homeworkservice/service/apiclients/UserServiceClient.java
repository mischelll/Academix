package com.academix.homeworkservice.service.apiclients;

import com.academix.homeworkservice.config.FeignConfig;
import com.academix.homeworkservice.service.dto.UserMetaDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "userClient", url = "${services.user.url}", configuration = FeignConfig.class)
public interface UserServiceClient {

    @GetMapping("/users/internal/me")
    UserMetaDTO getUserByEmail(@RequestParam("email") String email);
}
