package com.academix.user.web;

import com.academix.user.dao.User;
import com.academix.user.dao.repository.UserRepository;
import com.nimbusds.jose.shaded.gson.JsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UserController {

    Logger logger = LoggerFactory.getLogger(this.getClass());

    private final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public record UserDTO(String username, String email, String firstName) {
    }

    @GetMapping("/me")
    public ResponseEntity<UserDTO> getMe() {
        logger.info("GET /me");
        return ResponseEntity.ok(new UserDTO("me", "me@academix.com", "Me"));
    }

    @GetMapping("/protected/me")
    public ResponseEntity<UserDTO> getProtectedMe(@AuthenticationPrincipal String userId) {
        logger.info("GET /protected/me");
        logger.info(userId);
        Optional<User> byId = userRepository.findById(Long.valueOf(userId));
        if (byId.isPresent()) {
            User user = byId.get();
            return ResponseEntity.ok(new UserDTO(user.getUsername(), user.getEmail(), user.getFirstName()));
        }

        return ResponseEntity.status(HttpStatusCode.valueOf(401)).build();
    }
}
