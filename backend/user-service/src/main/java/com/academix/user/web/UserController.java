package com.academix.user.web;

import com.academix.user.dao.User;
import com.nimbusds.jose.shaded.gson.JsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("/api/users")
public class UserController {

    Logger logger = LoggerFactory.getLogger(this.getClass());

    public record UserDTO(String username) {}

    @GetMapping("/me")
    public ResponseEntity<UserDTO> getMe() {
        logger.info("GET /me");
        return ResponseEntity.ok(new UserDTO("me"));
    }

    @GetMapping("/protected/me")
    public ResponseEntity<UserDTO> getProtectedMe(@AuthenticationPrincipal String userId) {
        logger.info("GET /protected/me");
        logger.info(userId);
        return ResponseEntity.ok(new UserDTO("me"));
    }
}
