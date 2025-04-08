package com.academix.user.web;

import com.nimbusds.jose.shaded.gson.JsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
