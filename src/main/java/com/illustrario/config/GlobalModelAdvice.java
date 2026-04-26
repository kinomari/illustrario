package com.illustrario.config;

import com.illustrario.model.User;
import com.illustrario.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class GlobalModelAdvice {

    private final UserRepository userRepository;

    public GlobalModelAdvice(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @ModelAttribute("currentNickname")
    public String currentNickname(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) return null;
        return userRepository.findByEmail(authentication.getName())
            .map(User::getNickname).orElse(null);
    }

    @ModelAttribute("currentAvatarPath")
    public String currentAvatarPath(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) return null;
        return userRepository.findByEmail(authentication.getName())
            .map(User::getAvatarPath).orElse(null);
    }
}