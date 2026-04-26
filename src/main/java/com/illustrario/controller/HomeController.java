package com.illustrario.controller;

import com.illustrario.model.DailyTheme;
import com.illustrario.service.ArtworkService;
import com.illustrario.service.DailyThemeService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

@Controller
public class HomeController {

    private static final int HOME_PAGE_SIZE = 18;

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
        var firstPage = artworkService.getRecentArtworksPage(0, HOME_PAGE_SIZE);

        model.addAttribute("theme", todayTheme);
        model.addAttribute("recentArtworks", firstPage.getContent());
        model.addAttribute("recentArtworksHasMore", firstPage.hasNext());
        model.addAttribute("recentArtworksNextPage", 1);
        model.addAttribute("recentArtworksPageSize", HOME_PAGE_SIZE);

        return "index";
    }

    @GetMapping("/api/home/artworks")
    @ResponseBody
    public Map<String, Object> recentArtworks(@RequestParam(defaultValue = "0") int page,
                                              @RequestParam(defaultValue = "18") int size) {
        int safePage = Math.max(page, 0);
        int safeSize = Math.min(Math.max(size, 1), 30);
        var result = artworkService.getRecentArtworksPage(safePage, safeSize);

        List<Map<String, Object>> artworks = result.getContent().stream()
            .map(a -> Map.<String, Object>of(
                "id", a.getId(),
                "title", a.getTitle(),
                "artistName", a.getArtistName() == null ? "" : a.getArtistName(),
                "filePath", a.getFilePath()
            ))
            .toList();

        return Map.of(
            "artworks", artworks,
            "hasMore", result.hasNext(),
            "nextPage", safePage + 1
        );
    }
}
