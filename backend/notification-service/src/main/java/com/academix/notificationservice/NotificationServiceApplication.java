package com.academix.notificationservice;

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



    public static void main(String[] args) {
        SpringApplication.run(NotificationServiceApplication.class, args);
    }


}
