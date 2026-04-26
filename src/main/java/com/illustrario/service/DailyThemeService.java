package com.illustrario.service;

import com.illustrario.model.DailyTheme;
import com.illustrario.model.ThemeWord;
import com.illustrario.repository.DailyThemeRepository;
import com.illustrario.repository.ThemeWordRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Random;

@Service
public class DailyThemeService {

    private final DailyThemeRepository dailyThemeRepository;
    private final ThemeWordRepository themeWordRepository;
    private final Random random = new Random();

    public DailyThemeService(DailyThemeRepository dailyThemeRepository,
                              ThemeWordRepository themeWordRepository) {
        this.dailyThemeRepository = dailyThemeRepository;
        this.themeWordRepository = themeWordRepository;
    }

    @Transactional
    public DailyTheme getTodayTheme() {
        return dailyThemeRepository
            .findByDate(LocalDate.now())
            .orElseGet(this::drawAndSaveTheme);
    }

    @Transactional
    public DailyTheme drawAndSaveTheme() {
        return drawAndSaveThemeForDate(LocalDate.now());
    }

    @Transactional
    public DailyTheme drawAndSaveThemeForDate(LocalDate date) {

        return dailyThemeRepository.findByDate(date).orElseGet(() -> {
            ThemeWord chosen = pickWord();
            chosen.incrementTimesUsed();
            themeWordRepository.save(chosen);

            DailyTheme theme = new DailyTheme(date, chosen.getWord(), chosen.getHint());
            return dailyThemeRepository.save(theme);
        });
    }

    private ThemeWord pickWord() {
        List<ThemeWord> candidates = themeWordRepository.findLeastUsed();

        if (candidates.isEmpty()) {
    return new ThemeWord("carregando...", "Primeiro tema em breve!");
}

        return candidates.get(random.nextInt(candidates.size()));
    }
}