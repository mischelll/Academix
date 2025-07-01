package com.academix.userservice.security.oauth2;

import com.academix.userservice.dao.RefreshToken;
import com.academix.userservice.dao.Role;
import com.academix.userservice.dao.RoleEnum;
import com.academix.userservice.dao.User;
import com.academix.userservice.repository.RoleRepository;
import com.academix.userservice.repository.UserRepository;
import com.academix.userservice.security.jwt.JwtTokenProvider;
import com.academix.userservice.service.RefreshService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class OAuth2LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtTokenProvider tokenProvider;
    private final RefreshService refreshService;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws java.io.IOException {
        System.out.println("✅ Successful login callback reached");

        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        User user = handleUserAuthentication(oAuth2User);

        String jwt = tokenProvider.generateToken(user.getEmail());
        RefreshToken refreshToken = refreshService.createRefreshToken(user.getId());

        ResponseCookie cookie = ResponseCookie.from("refreshToken", refreshToken.getToken())
                .httpOnly(true)
                .secure(false)
                .path("/")
                .maxAge(Duration.ofDays(7))
                .sameSite("Lax")
                .build();

        response.setHeader("Set-Cookie", cookie.toString());

        String redirectUrl = "http://localhost:5173/oauth-success?token=" + jwt;
        getRedirectStrategy().sendRedirect(request, response, redirectUrl);
    }

    private User handleUserAuthentication(OAuth2User oAuth2User) {
        String email = oAuth2User.getAttribute("email");
        String name = oAuth2User.getAttribute("name");
        String familyName = oAuth2User.getAttribute("family_name");
        String picture = oAuth2User.getAttribute("picture");
        boolean isVerified = Boolean.parseBoolean(oAuth2User.getAttribute("verified"));

        // Determine role based on email (for testing purposes)
        Set<Role> roles;
        if ("teacher@test.com".equals(email)) {
            roles = Set.of(roleRepository.findByName(RoleEnum.ROLE_TEACHER));
        } else if ("admin@test.com".equals(email)) {
            roles = Set.of(roleRepository.findByName(RoleEnum.ROLE_ADMIN));
        } else {
            roles = Set.of(roleRepository.findByName(RoleEnum.ROLE_STUDENT));
        }

        User userBuilt = User.builder()
                .username(email)
                .avatar(picture)
                .isVerified(isVerified)
                .password("test-password")
                .email(email)
                .firstName(name)
                .lastName(familyName)
                .phone("+35988999999")
                .city("Sofia")
                .zip("1000")
                .country("Bulgaria")
                .roles(roles)
                .build();

        return userRepository.findByEmail(email).orElseGet(() -> userRepository.save(userBuilt));
    }
}
