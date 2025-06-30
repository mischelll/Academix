package com.academix.notificationservice.client;

import com.academix.notificationservice.client.dto.UserDTO;
import org.springframework.stereotype.Service;

@Service
public class UserServiceClientImpl implements UserServiceClient {
    @Override
    public String getTeacherEmailByLessonId(Long lessonId) {
        return "";
    }

    @Override
    public UserDTO getUserById(Long studentId) {
        return null;
    }
}
