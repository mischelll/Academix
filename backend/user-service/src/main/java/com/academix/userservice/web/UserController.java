package com.academix.userservice.web;

import com.academix.userservice.dao.Role;
import com.academix.userservice.dao.User;
import com.academix.userservice.repository.UserRepository;
import com.academix.userservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final Logger logger = LoggerFactory.getLogger(UserController.class);

    private final UserRepository userRepository;
    private final UserService userService;

    public record UserDTO(Long id, String username, String email, String firstName, String avatar, Set<Role> roles) {
    }

    public record UserUpdateDTO(Long id, String username, String avatar, String phone) {
    }


    @GetMapping("/protected/me")
    public ResponseEntity<UserDTO> getProtectedMe(@AuthenticationPrincipal String userId) {
        logger.info("GET /protected/me");
        Optional<User> byId = userRepository.findById(Long.valueOf(userId));
        if (byId.isPresent()) {
            User user = byId.get();
            return ResponseEntity.ok(new UserDTO(
                    user.getId(),
                    user.getUsername(),
                    user.getEmail(),
                    user.getFirstName(),
                    user.getAvatar(),
                    user.getRoles())
            );
        }

        return ResponseEntity.status(HttpStatusCode.valueOf(401)).build();
    }

    @PostMapping("/user-state")
    public ResponseEntity<User> updateUserState(@RequestBody UserUpdateDTO userDTO) {
        logger.info("POST /user-state");
        return ResponseEntity.ok(userService.updateUser(userDTO));
    }
}
