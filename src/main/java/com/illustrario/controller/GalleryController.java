package com.illustrario.controller;

import com.illustrario.service.ArtworkService;
import com.illustrario.service.DailyThemeService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

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
        model.addAttribute("artworks", artworkService.getPublicArtworksByTheme(todayTheme.getWord()));
        return "gallery/index";
    }
}