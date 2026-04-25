package com.illustrario.controller;

import com.illustrario.model.DailyTheme;
import com.illustrario.model.ThemeWord;
import com.illustrario.repository.DailyThemeRepository;
import com.illustrario.repository.ThemeWordRepository;
import com.illustrario.service.DailyThemeService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/admin/themes")
@PreAuthorize("hasRole('CURATOR')")
public class AdminThemeController {

    private final DailyThemeRepository dailyThemeRepository;
    private final ThemeWordRepository themeWordRepository;
    private final DailyThemeService dailyThemeService;

    public AdminThemeController(DailyThemeRepository dailyThemeRepository,
                                ThemeWordRepository themeWordRepository,
                                DailyThemeService dailyThemeService) {
        this.dailyThemeRepository = dailyThemeRepository;
        this.themeWordRepository = themeWordRepository;
        this.dailyThemeService = dailyThemeService;
    }

    @GetMapping
    public String themes(Model model) {
        List<DailyTheme> recent = dailyThemeRepository
            .findByDateBeforeOrderByDateDesc(LocalDate.now().plusDays(30))
            .stream().limit(30).toList();

        model.addAttribute("themes", recent);
        model.addAttribute("today", LocalDate.now());
        model.addAttribute("words", themeWordRepository.findAll());
        model.addAttribute("todayTheme", dailyThemeService.getTodayTheme());
        return "admin/themes";
    }

    @PostMapping("/set")
    public String setTheme(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam String word,
            @RequestParam(required = false) String hint,
            RedirectAttributes ra) {

        ThemeWord themeWord = themeWordRepository.findAll().stream()
            .filter(w -> w.getWord().equals(word)).findFirst().orElse(null);

        String finalHint = (hint != null && !hint.isBlank())
            ? hint : (themeWord != null ? themeWord.getHint() : "");

        DailyTheme theme = dailyThemeRepository.findByDate(date)
            .orElse(new DailyTheme(date, word, finalHint));
        theme.setWord(word);
        theme.setHint(finalHint);
        dailyThemeRepository.save(theme);

        ra.addFlashAttribute("successMessage",
            "Tema de " + date + " definido como \"" + word + "\".");
        return "redirect:/admin/themes";
    }

    @PostMapping("/draw")
    public String drawTheme(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            RedirectAttributes ra) {
        dailyThemeRepository.findByDate(date).ifPresent(dailyThemeRepository::delete);
        DailyTheme drawn = dailyThemeService.drawAndSaveThemeForDate(date);
        ra.addFlashAttribute("successMessage",
            "Sorteado para " + date + ": \"" + drawn.getWord() + "\".");
        return "redirect:/admin/themes";
    }

    @PostMapping("/words/add")
    public String addWord(@RequestParam String word,
                          @RequestParam(required = false) String hint,
                          RedirectAttributes ra) {
        if (word == null || word.isBlank()) {
            ra.addFlashAttribute("errorMessage", "Palavra não pode ser vazia.");
            return "redirect:/admin/themes";
        }
        boolean exists = themeWordRepository.findAll().stream()
            .anyMatch(w -> w.getWord().equalsIgnoreCase(word.trim()));
        if (exists) {
            ra.addFlashAttribute("errorMessage", "Palavra já existe no pool.");
            return "redirect:/admin/themes";
        }
        themeWordRepository.save(new ThemeWord(word.trim().toLowerCase(),
            hint != null ? hint.trim() : ""));
        ra.addFlashAttribute("successMessage", "\"" + word + "\" adicionada ao pool.");
        return "redirect:/admin/themes";
    }

    @PostMapping("/words/{id}/delete")
    public String deleteWord(@PathVariable Long id, RedirectAttributes ra) {
        themeWordRepository.deleteById(id);
        ra.addFlashAttribute("successMessage", "Palavra removida.");
        return "redirect:/admin/themes";
    }
}