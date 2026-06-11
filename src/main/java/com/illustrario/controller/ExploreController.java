package com.illustrario.controller;

import com.illustrario.model.Artwork;
import com.illustrario.model.DailyTheme;
import com.illustrario.repository.ArtworkRepository;
import com.illustrario.repository.DailyThemeRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Controller
public class ExploreController {
    private static final int THEMES_PER_PAGE = 10;

    private final DailyThemeRepository dailyThemeRepository;
    private final ArtworkRepository artworkRepository;

    public ExploreController(DailyThemeRepository dailyThemeRepository,
                             ArtworkRepository artworkRepository) {
        this.dailyThemeRepository = dailyThemeRepository;
        this.artworkRepository = artworkRepository;
    }

    @GetMapping("/explorar")
    public String explore(@RequestParam(defaultValue = "0") int page, Model model) {
        int currentPage = Math.max(page, 0);

        Page<DailyTheme> pastThemes =
            dailyThemeRepository.findByDateBeforeOrderByDateDesc(
                LocalDate.now(),
                PageRequest.of(currentPage, THEMES_PER_PAGE)
            );

        if (pastThemes.getTotalPages() > 0 && currentPage >= pastThemes.getTotalPages()) {
            currentPage = pastThemes.getTotalPages() - 1;
            pastThemes = dailyThemeRepository.findByDateBeforeOrderByDateDesc(
                LocalDate.now(),
                PageRequest.of(currentPage, THEMES_PER_PAGE)
            );
        }

        Map<DailyTheme, List<Artwork>> themeArtworks = new LinkedHashMap<>();

        for (DailyTheme theme : pastThemes.getContent()) {

            List<Artwork> artworks =
                artworkRepository
                    .findTop12ByThemeAndRemovedFalseOrderByUploadedAtDesc(
                        theme.getWord()
                    );

            themeArtworks.put(theme, artworks);
        }

        model.addAttribute("themeArtworks", themeArtworks);
        model.addAttribute("themePage", pastThemes);
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("previousPage", Math.max(currentPage - 1, 0));
        model.addAttribute("nextPage", Math.min(currentPage + 1, Math.max(pastThemes.getTotalPages() - 1, 0)));

        return "explore";
    }
}
