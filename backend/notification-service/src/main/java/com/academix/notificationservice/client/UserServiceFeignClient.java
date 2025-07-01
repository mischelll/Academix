package com.academix.notificationservice.client;

import com.academix.notificationservice.config.FeignConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "userServiceClient", url = "${services.user.url}", configuration = FeignConfig.class)
public interface UserServiceFeignClient {

    @GetMapping("/api/users/internal/{userId}")
    UserMetaResponse getUserById(@PathVariable("userId") Long userId);

    record UserMetaResponse(Long id, String name, String email, String phone, java.util.Collection<String> roles) {}
} 