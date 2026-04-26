package com.illustrario.controller;

import com.illustrario.repository.UserRepository;
import com.illustrario.service.ArtworkService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/perfil")
public class PublicProfileController {

    private final UserRepository userRepository;
    private final ArtworkService artworkService;

    public PublicProfileController(UserRepository userRepository,
                                   ArtworkService artworkService) {
        this.userRepository = userRepository;
        this.artworkService = artworkService;
    }

    @GetMapping("/{nickname}")
    public String publicProfile(@PathVariable String nickname, Model model) {
        var user = userRepository.findByNickname(nickname)
            .orElseThrow(() -> new IllegalArgumentException("Artista não encontrado: " + nickname));

        model.addAttribute("user", user);
        model.addAttribute("artworks",
            artworkService.getArtworksByUser(user)
                .stream().filter(a -> !a.isRemoved()).toList());

        return "public_profile";
    }
}