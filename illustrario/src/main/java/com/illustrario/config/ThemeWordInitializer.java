package com.illustrario.config;

import com.illustrario.service.DailyThemeService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class ThemeWordInitializer implements CommandLineRunner {

    private final DailyThemeService dailyThemeService;

    public ThemeWordInitializer(DailyThemeService dailyThemeService) {
        this.dailyThemeService = dailyThemeService;
    }

    @Override
    public void run(String... args) {
        try {
            var tema = dailyThemeService.getTodayTheme();
            System.out.println("🎨 Tema de hoje: " + tema.getWord());
        } catch (IllegalStateException e) {
            System.out.println("⚠️  Sem tema hoje, ops...");
        }
    }
}