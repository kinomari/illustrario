package com.illustrario.config;

import com.illustrario.model.ThemeWord;
import com.illustrario.repository.ThemeWordRepository;
import com.illustrario.service.DailyThemeService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ThemeWordInitializer implements CommandLineRunner {

    private final ThemeWordRepository themeWordRepository;
    private final DailyThemeService dailyThemeService;

    public ThemeWordInitializer(ThemeWordRepository themeWordRepository,
                                DailyThemeService dailyThemeService) {
        this.themeWordRepository = themeWordRepository;
        this.dailyThemeService = dailyThemeService;
    }

    @Override
    public void run(String... args) {
        if (themeWordRepository.count() == 0) {
            List<ThemeWord> pool = List.of(
                new ThemeWord("concha",      "Que remete a praia, ao mar, ao barulho das ondas..."),
                new ThemeWord("nuvem",       "Desde a branquinha, fofinha, algodão doce, à tempestuosa..."),
                new ThemeWord("divindade",   "Que reina sobre o céu e a terra, puro, sagrado..."),
                new ThemeWord("sonho",       "Surreal, onírico, fantástico, sem lógica..."),
                new ThemeWord("memória",     "Uma cena do passado, nostalgia, saudade..."),
                new ThemeWord("fogo",        "Chamas, calor, destruição e renascimento..."),
                new ThemeWord("silêncio",    "O que você vê quando tudo para?"),
                new ThemeWord("raiz",        "Origem, família, pertencimento, chão..."),
                new ThemeWord("jardim",      "Flores, cuidado, crescimento, tempo..."),
                new ThemeWord("sombra",      "O que a luz deixa para trás..."),
                new ThemeWord("infância",    "Brinquedos, inocência, um lugar de antes..."),
                new ThemeWord("sol",         "Dia, luz, brilho, claridade..."),
                new ThemeWord("máscara",     "O que mostramos, o que escondemos..."),
                new ThemeWord("lua",         "Noite, ciclos, mistério, marés...")
            );
            themeWordRepository.saveAll(pool);
            System.out.println("✅ Pool de " + pool.size() + " palavras-tema carregado!");
        }

        var tema = dailyThemeService.getTodayTheme();
        System.out.println("🎨 Tema de hoje: " + tema.getWord());
    }
}