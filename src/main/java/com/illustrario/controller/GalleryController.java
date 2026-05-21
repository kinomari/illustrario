package com.illustrario.controller;

import com.illustrario.dto.ArtworkUploadDto;
import com.illustrario.service.ArtworkService;
import com.illustrario.service.DailyThemeService;
import jakarta.validation.Valid;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;

@Controller
@RequestMapping("/gallery")
public class GalleryController {

    private final ArtworkService artworkService;
    private final DailyThemeService dailyThemeService;

    public GalleryController(ArtworkService artworkService,
                             DailyThemeService dailyThemeService) {
        this.artworkService = artworkService;
        this.dailyThemeService = dailyThemeService;
    }

    @GetMapping
    public String gallery(Model model) {
        var todayTheme = dailyThemeService.getTodayTheme();
        model.addAttribute("theme", todayTheme);
        model.addAttribute("artworks", artworkService.getArtworksByTheme(todayTheme.getWord()));
        model.addAttribute("uploadDto", new ArtworkUploadDto());
        model.addAttribute("artworks", artworkService.getPublicArtworksByTheme(todayTheme.getWord()));
        return "gallery/index";
    }

    @PostMapping("/upload")
    public String upload(@Valid @ModelAttribute("uploadDto") ArtworkUploadDto dto,
                         BindingResult bindingResult,
                         @AuthenticationPrincipal UserDetails userDetails,
                         RedirectAttributes redirectAttributes,
                         Model model) {

        var todayTheme = dailyThemeService.getTodayTheme();

        if (bindingResult.hasErrors()) {
            model.addAttribute("theme", todayTheme);
            model.addAttribute("artworks", artworkService.getArtworksByTheme(todayTheme.getWord()));
            return "gallery/index";
        }

        try {
            artworkService.upload(dto, todayTheme.getWord(), userDetails.getUsername());
            redirectAttributes.addFlashAttribute("successMessage", "Arte enviada com sucesso! 🎨");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        } catch (IOException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Erro ao salvar a imagem.");
        }

        return "redirect:/gallery";
    }
}