package com.academix.notificationservice.client;

import com.academix.notificationservice.client.dto.UserDTO;
import org.springframework.stereotype.Service;

@Service
public interface UserServiceClient {

    String getTeacherEmailByLessonId(Long lessonId);

    UserDTO getUserById(Long studentId);
}
