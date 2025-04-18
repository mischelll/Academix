package com.academix.user.security.oauth2;

import com.academix.user.dao.User;
import com.academix.user.dao.repository.UserRepository;
import com.academix.user.security.jwt.JwtTokenProvider;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

@Component
public class OAuth2LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtTokenProvider tokenProvider;

    private final UserRepository userRepository;

    public OAuth2LoginSuccessHandler(JwtTokenProvider tokenProvider, UserRepository userRepository) {
        this.tokenProvider = tokenProvider;
        this.userRepository = userRepository;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws java.io.IOException {
        System.out.println("✅ Successful login callback reached");
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        String email = oAuth2User.getAttribute("email");
        String name = oAuth2User.getAttribute("name");
        String familyName = oAuth2User.getAttribute("family_name");
        String picture = oAuth2User.getAttribute("picture");
        boolean isVerified = Boolean.parseBoolean(oAuth2User.getAttribute("verified"));

        // Fetch or create user
        User build = User.builder()
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
                .build();
        User user = userRepository.findByEmail(email)
                .orElseGet(() -> userRepository.save(build));

        String jwt = tokenProvider.generateToken(user.getId());

        String redirectUrl = "http://localhost:5173/oauth-success?token=" + jwt;
        getRedirectStrategy().sendRedirect(request, response, redirectUrl);
    }
}
