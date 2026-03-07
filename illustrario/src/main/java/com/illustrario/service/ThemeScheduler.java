package com.illustrario.service;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalTime;

@Component
public class ThemeScheduler {

    private final DailyThemeService dailyThemeService;

    public ThemeScheduler(DailyThemeService dailyThemeService) {
        this.dailyThemeService = dailyThemeService;
    }

    @Scheduled(cron = "0 0 0 * * *", zone = "America/Sao_Paulo")
    public void sortearTemaDoDia() {
        System.out.println("[ThemeScheduler] Sorteando tema para " + LocalDate.now()
            + " às " + LocalTime.now());
        dailyThemeService.drawAndSaveTheme();
        System.out.println("[ThemeScheduler] Tema sorteado com sucesso!");
    }
}