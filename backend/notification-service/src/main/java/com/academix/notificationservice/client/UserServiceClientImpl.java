package com.academix.notificationservice.client;

import com.academix.notificationservice.client.dto.UserDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Collection;

@Service
public class UserServiceClientImpl implements UserServiceClient {
    
    private static final Logger logger = LoggerFactory.getLogger(UserServiceClientImpl.class);
    
    @Value("${services.user.url:http://localhost:8081}")
    private String userServiceUrl;
    
    @Value("${api.internalApiKey:internal-secret-key}")
    private String internalApiKey;
    
    private final RestTemplate restTemplate = new RestTemplate();

    @Override
    public String getTeacherEmailByLessonId(Long lessonId) {
        try {
            // This would need to be implemented in curriculum service
            // For now, return a default email
            return "teacher@academix.com";
        } catch (Exception e) {
            logger.error("Error getting teacher email for lessonId: {}", lessonId, e);
            return "teacher@academix.com"; // fallback
        }
    }

    @Override
    public UserDTO getUserById(Long studentId) {
        logger.info("=== getUserById called with studentId: {} ===", studentId);
        logger.info("userServiceUrl: {}", userServiceUrl);
        logger.info("internalApiKey length: {}", internalApiKey != null ? internalApiKey.length() : "null");
        
        if (studentId == null) {
            logger.error("studentId is null!");
            return null;
        }
        
        try {
            String url = userServiceUrl + "/api/users/internal/" + studentId;
            logger.info("Fetching user with ID: {} from URL: {}", studentId, url);
            logger.info("Using internal API key: {}", internalApiKey != null ? 
                internalApiKey.substring(0, Math.min(10, internalApiKey.length())) + "..." : "null");
            
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + internalApiKey);
            HttpEntity<String> entity = new HttpEntity<>(headers);
            
            logger.info("Making HTTP request with headers: {}", headers);
            
            ResponseEntity<UserResponse> response = restTemplate.exchange(
                url, HttpMethod.GET, entity, UserResponse.class
            );
            
            logger.info("Received response status: {}", response.getStatusCode());
            
            if (response.getBody() != null) {
                UserResponse userResponse = response.getBody();
                logger.info("User found: ID={}, Name={}, Email={}, Phone={}", 
                    userResponse.id(), userResponse.name(), userResponse.email(), userResponse.phone());
                return new UserDTO(userResponse.email(), userResponse.name(), userResponse.phone());
            } else {
                logger.warn("User not found for ID: {}", studentId);
                return null;
            }
        } catch (Exception e) {
            logger.error("Error fetching user with ID: {}", studentId, e);
            logger.error("Exception type: {}", e.getClass().getSimpleName());
            logger.error("Exception message: {}", e.getMessage());
            
            // Try a simple test to see if the service is reachable
            try {
                String testUrl = userServiceUrl + "/actuator/health";
                logger.info("Testing service reachability at: {}", testUrl);
                String healthResponse = restTemplate.getForObject(testUrl, String.class);
                logger.info("Health check response: {}", healthResponse);
            } catch (Exception healthEx) {
                logger.error("Health check failed: {}", healthEx.getMessage());
            }
            
            return null;
        }
    }
    
    // Internal response class to match the user service response
    private record UserResponse(Long id, String name, String email, String phone, Collection<String> roles) {}
}
