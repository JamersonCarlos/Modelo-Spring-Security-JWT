package com.spring.security.jwt.services;

import com.spring.security.jwt.models.Role;
import com.spring.security.jwt.models.User;
import com.spring.security.jwt.repository.UserRepository;
import com.spring.security.jwt.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    @Autowired
    private  UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;


    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        // Extrair dados do Google
        String email = oAuth2User.getAttribute("email");
        String name = oAuth2User.getAttribute("name");

        // Lógica de "Upsert" (Update ou Insert)
        Optional<User> userOptional = userRepository.findByUsername(email);
        
        User user;
        if (userOptional.isPresent()) {
            user = userOptional.get();
            // Atualiza dados se necessário
            user.setName(name);
            userRepository.save(user);
        } else {
            // Cria novo usuário vindo do Google
            Optional<Role> optRole = roleRepository.findByName("BASIC");
            Role role = optRole.orElseThrow(() -> new RuntimeException("Role not found"));

            user = new User();
            user.setUsername(email);
            user.setName(name);
            user.setPassword("");
            user.setAuthProvider("GOOGLE"); 
            user.setRoles(Set.of(role));
            userRepository.save(user);
        }

        return oAuth2User;
    }
}