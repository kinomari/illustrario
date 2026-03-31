package com.illustrario.controller;

import com.illustrario.model.User;
import com.illustrario.repository.UserRepository;
import com.illustrario.service.ArtworkService;
import com.illustrario.service.ProfileService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/profile")
public class ProfileController {

    private final UserRepository userRepository;
    private final ArtworkService artworkService;
    private final ProfileService profileService;

    public ProfileController(UserRepository userRepository,
                             ArtworkService artworkService,
                             ProfileService profileService) {
        this.userRepository = userRepository;
        this.artworkService = artworkService;
        this.profileService = profileService;
    }

    @GetMapping
    public String profile(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        User user = userRepository.findByEmail(userDetails.getUsername())
            .orElseThrow();

        model.addAttribute("user", user);
        model.addAttribute("artworks", artworkService.getArtworksByUser(user)
            .stream().filter(a -> !a.isRemoved()).toList());

        return "profile";
    }

    @PostMapping("/avatar")
    public String updateAvatar(@RequestParam("avatarFile") MultipartFile file,
                               @AuthenticationPrincipal UserDetails userDetails,
                               RedirectAttributes ra) {
        try {
            profileService.updateAvatar(userDetails.getUsername(), file);
            ra.addFlashAttribute("successMessage", "Avatar atualizado!");
        } catch (Exception e) {
            ra.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/profile";
    }
}