package com.illustrario.controller;

import com.illustrario.model.Artwork;
import com.illustrario.model.DailyTheme;
import com.illustrario.repository.ArtworkRepository;
import com.illustrario.repository.DailyThemeRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Controller
public class ExploreController {

    private final DailyThemeRepository dailyThemeRepository;
    private final ArtworkRepository artworkRepository;

    public ExploreController(DailyThemeRepository dailyThemeRepository,
                             ArtworkRepository artworkRepository) {
        this.dailyThemeRepository = dailyThemeRepository;
        this.artworkRepository = artworkRepository;
    }

    @GetMapping("/explorar")
    public String explore(Model model) {
    
        List<DailyTheme> pastThemes = dailyThemeRepository
            .findByDateBeforeOrderByDateDesc(LocalDate.now());

        Map<DailyTheme, List<Artwork>> themeArtworks = new LinkedHashMap<>();
        for (DailyTheme theme : pastThemes) {
            List<Artwork> artworks = artworkRepository
                .findByThemeOrderByUploadedAtDesc(theme.getWord())
                .stream()
                .filter(a -> !a.isRemoved())
                .toList();
            themeArtworks.put(theme, artworks);
        }

        model.addAttribute("themeArtworks", themeArtworks);
        return "explore";
    }
}