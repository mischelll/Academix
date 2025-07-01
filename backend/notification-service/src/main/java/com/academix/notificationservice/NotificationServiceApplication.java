package com.academix.notificationservice;

import com.academix.notificationservice.client.UserServiceClient;
import com.academix.notificationservice.client.dto.UserDTO;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@EnableFeignClients
@RestController
public class NotificationServiceApplication {

    private final UserServiceClient userServiceClient;

    public NotificationServiceApplication(UserServiceClient userServiceClient) {
        this.userServiceClient = userServiceClient;
    }

    public static void main(String[] args) {
        SpringApplication.run(NotificationServiceApplication.class, args);
    }

    @GetMapping("/test/user/{userId}")
    public String testUserService(@PathVariable Long userId) {
        try {
            UserDTO user = userServiceClient.getUserByIdAsDTO(userId);
            if (user != null) {
                return "User found: " + user.firstName() + " (" + user.email() + ")";
            } else {
                return "User not found for ID: " + userId;
            }
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }
}
