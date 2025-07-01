package com.academix.notificationservice.client;

import com.academix.notificationservice.client.dto.UserDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "userServiceClient", url = "${services.user.url}")
public interface UserServiceClient {

    @GetMapping("/api/users/internal/{userId}")
    UserMetaResponse getUserById(@PathVariable("userId") Long userId);

    @GetMapping("/api/users/internal/teacher-email")
    String getTeacherEmailByLessonId(@PathVariable("lessonId") Long lessonId);

    record UserMetaResponse(Long id, String name, String email, String phone, java.util.Collection<String> roles) {}
    
    // Default method to convert UserMetaResponse to UserDTO
    default UserDTO getUserByIdAsDTO(Long userId) {
        try {
            UserMetaResponse response = getUserById(userId);
            if (response != null) {
                return new UserDTO(response.email(), response.name(), response.phone());
            }
            return null;
        } catch (Exception e) {
            return null;
        }
    }
}
