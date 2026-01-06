package com.spring.security.jwt.security;

import com.spring.security.jwt.models.Role;
import com.spring.security.jwt.models.User;
import com.spring.security.jwt.repository.UserRepository;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.time.Instant;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;

import com.spring.security.jwt.services.UserService;

@Component
public class OAuth2LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    @Autowired
    private JwtEncoder jwtEncoder;

    @Autowired 
    private UserService userService; 
    
    // URL do seu Front-end onde ele captura o token
    private final String FRONTEND_URL = "http://localhost:5173/auth/callback"; 

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, 
                                        HttpServletResponse response, 
                                        Authentication authentication) throws IOException, ServletException {

        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        String email = oAuth2User.getAttribute("email");

        Optional<User> userOpt = userService.isUserActive(email);

        User user = userOpt.get();
        var now = Instant.now();
        var expiresIn = 300L;

        var scopes = user.getRoles().stream().map(Role::getName).collect(Collectors.joining(" "));

        var claims = JwtClaimsSet.builder().issuer("mybackend").subject(user.getId().toString())
            .expiresAt(now.plusSeconds(expiresIn)).claim("scope", scopes).issuedAt(now).build();

        var jwtValue = jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();


        // 2. Monta a URL de redirecionamento para o Front
        String targetUrl = UriComponentsBuilder.fromUriString(FRONTEND_URL)
                .queryParam("token", jwtValue)
                .build().toUriString();

        // 3. Redireciona
        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }
}