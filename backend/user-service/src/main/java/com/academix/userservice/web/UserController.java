package com.academix.userservice.web;

import com.academix.userservice.dao.Role;
import com.academix.userservice.dao.RoleEnum;
import com.academix.userservice.dao.User;
import com.academix.userservice.repository.RoleRepository;
import com.academix.userservice.repository.UserRepository;
import com.academix.userservice.service.UserService;
import com.academix.userservice.service.dto.UserMetaDTO;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
    private final RoleRepository roleRepository;

    public record UserDTO(Long id, String username, String email, String firstName, String avatar, Set<Role> roles) {
    }

    public record UserUpdateDTO(Long id, String username, String avatar, String phone) {
    }

    public record ChangeRoleRequest(String email, String role) {
    }

    @GetMapping("/protected/me")
    public ResponseEntity<UserDTO> getProtectedMe(@AuthenticationPrincipal String email) {
        logger.info("GET /protected/me");
        Optional<User> byId = userRepository.findByEmail(email);
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

    @GetMapping("/internal/me")
    public ResponseEntity<UserMetaDTO> getInternaldMe(@RequestParam String email) {
        logger.info("GET /internal/me");
        UserMetaDTO userMeta = userService.getUserMeta(email);
        return ResponseEntity.ok(userMeta);
    }

    @GetMapping("/internal/{userId}")
    public ResponseEntity<UserMetaDTO> getInternalMe(@PathVariable Long userId) {
        logger.info("GET /internal/{}" ,userId);
        UserMetaDTO userMeta = userService.getUserMeta(userId);
        return ResponseEntity.ok(userMeta);
    }

    @PostMapping("/admin/change-role")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> changeUserRole(@RequestBody ChangeRoleRequest request) {
        logger.info("Changing role for user: {} to: {}", request.email, request.role);
        
        Optional<User> userOpt = userRepository.findByEmail(request.email);
        if (userOpt.isEmpty()) {
            return ResponseEntity.badRequest().body("User not found");
        }

        RoleEnum roleEnum;
        try {
            roleEnum = RoleEnum.valueOf(request.role);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Invalid role: " + request.role);
        }

        Role role = roleRepository.findByName(roleEnum);
        if (role == null) {
            return ResponseEntity.badRequest().body("Role not found in database");
        }

        User user = userOpt.get();
        user.getRoles().clear();
        user.addRole(role);
        userRepository.save(user);

        return ResponseEntity.ok("Role changed successfully");
    }
}
