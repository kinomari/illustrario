package com.illustrario.service;

import com.illustrario.model.User;
import com.illustrario.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository repo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public User register(User user) {
        user.setEmail(user.getEmail().trim().toLowerCase());
        user.setNickname(user.getNickname().trim());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole("ROLE_USER");
        return repo.save(user);
    }

    public User findByEmail(String email) {
        return repo.findByEmail(email.trim().toLowerCase()).orElse(null);
    }
}