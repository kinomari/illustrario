package com.illustrario.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.illustrario.repository.ArtRepository;
import com.illustrario.service.ThemeService;

@Controller
public class HomeController {
  private final ThemeService themeService;
  private final ArtRepository artRepository;
  public HomeController(ThemeService themeService, ArtRepository artRepository) {
    this.themeService = themeService; this.artRepository = artRepository;
  }

  @GetMapping("/")
  public String index(Model m) {
    var theme = themeService.getTodayTheme();
    if (theme == null) themeService.generateDailyTheme(); // garante
    m.addAttribute("theme", themeService.getTodayTheme());
    m.addAttribute("recentArts", artRepository.findTop20ByOrderByCreatedAtDesc());
    return "index";
  }
}