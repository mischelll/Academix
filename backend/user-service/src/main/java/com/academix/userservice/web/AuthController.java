package com.academix.userservice.web;

import com.academix.userservice.dao.RefreshToken;
import com.academix.userservice.security.jwt.JwtTokenProvider;
import com.academix.userservice.service.RefreshService;
import com.academix.userservice.service.dto.RefreshRequest;
import com.academix.userservice.service.dto.RefreshTokenResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {


    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshService refreshTokenService;

    @PostMapping("/refresh")
    public ResponseEntity<?> refreshToken(@CookieValue(value = "refreshToken", required = false) String refreshToken) {

        return refreshTokenService.findByToken(refreshToken)
                .map(refreshTokenService::verifyExpiration)
                .map(RefreshToken::getUser)
                .map(user -> {
                    String token = jwtTokenProvider.generateToken(user.getId());
                    return ResponseEntity.ok(new RefreshTokenResponse(token, refreshToken));
                })
                .orElseThrow(() -> new RuntimeException("Refresh token not found."));
    }
}
