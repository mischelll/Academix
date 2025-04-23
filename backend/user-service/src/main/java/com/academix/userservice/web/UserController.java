package com.academix.userservice.web;

import com.academix.userservice.dao.Role;
import com.academix.userservice.dao.User;
import com.academix.userservice.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("/api/users")
public class UserController {

    Logger logger = LoggerFactory.getLogger(this.getClass());

    private final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public record UserDTO(String username, String email, String firstName, String avatar, Set<Role> roles) {
    }

    @GetMapping("/protected/me")
    public ResponseEntity<UserDTO> getProtectedMe(@AuthenticationPrincipal String userId) {
        logger.info("GET /protected/me");
        Optional<User> byId = userRepository.findById(Long.valueOf(userId));
        if (byId.isPresent()) {
            User user = byId.get();
            return ResponseEntity.ok(new UserDTO(user.getUsername(), user.getEmail(), user.getFirstName(), user.getAvatar(), user.getRoles()));
        }

        return ResponseEntity.status(HttpStatusCode.valueOf(401)).build();
    }
}
