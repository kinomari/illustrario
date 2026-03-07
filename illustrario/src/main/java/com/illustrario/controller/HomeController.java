package com.illustrario.controller;

import com.illustrario.model.DailyTheme;
import com.illustrario.service.ArtworkService;
import com.illustrario.service.DailyThemeService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    private final DailyThemeService dailyThemeService;
    private final ArtworkService artworkService;

    public HomeController(DailyThemeService dailyThemeService,
                          ArtworkService artworkService) {
        this.dailyThemeService = dailyThemeService;
        this.artworkService = artworkService;
    }

    @GetMapping("/")
    public String index(Model model) {
        DailyTheme todayTheme = dailyThemeService.getTodayTheme();

        model.addAttribute("theme", todayTheme);
        model.addAttribute("recentArtworks", artworkService.getRecentArtworks());

        return "index";
    }
}