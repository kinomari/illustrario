package com.illustrario.service;

import com.illustrario.model.DailyTheme;
import com.illustrario.repository.DailyThemeRepository;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Random;

@Service
public class ThemeService {
  private final DailyThemeRepository repo;
  private final List<String> pool = List.of(
    "Saudade","Luz","Cidade","Silêncio","Mar","Noite","Memória","Ritmo","Amanhecer","Fragmento"
  );

  public ThemeService(DailyThemeRepository repo) { this.repo = repo; }

  public DailyTheme getTodayTheme() {
    var today = LocalDate.now();
    return repo.findByDate(today).orElse(null);
  }

  @Scheduled(cron = "0 0 0 * * *")
  public void generateDailyTheme() {
    LocalDate today = LocalDate.now();
    repo.findByDate(today).ifPresentOrElse(
      t -> { /* já existe */ },
      () -> {
        String theme = pool.get(new Random().nextInt(pool.size()));
        DailyTheme dt = new DailyTheme();
        dt.setDate(today);
        dt.setTheme(theme);
        repo.save(dt);
      }
    );
  }
}
