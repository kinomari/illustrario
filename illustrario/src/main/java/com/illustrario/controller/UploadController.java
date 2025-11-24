package com.illustrario.controller;

import com.illustrario.model.Art;
import com.illustrario.repository.ArtRepository;
import com.illustrario.service.FileStorageService;
import com.illustrario.service.ThemeService;
import com.illustrario.model.User;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

@Controller
public class UploadController {

    private final FileStorageService fileService;
    private final ArtRepository artRepository;
    private final ThemeService themeService;

    public UploadController(FileStorageService fileService,
                            ArtRepository artRepository,
                            ThemeService themeService) {
        this.fileService = fileService;
        this.artRepository = artRepository;
        this.themeService = themeService;
    }

    @GetMapping("/upload")
    public String uploadPage() {
        return "upload";
    }

    @PostMapping("/upload")
    public String uploadArt(@RequestParam String title,
                            @RequestParam String description,
                            @RequestParam MultipartFile image,
                            Model model) {

        try {
            String savedPath = fileService.saveFile(image);

            Art art = new Art();
            art.setTitle(title);
            art.setDescription(description);
            art.setImagePath(savedPath);
            art.setCreatedAt(LocalDateTime.now());
            art.setThemeOfDay(themeService.getTodayTheme());

            User fakeUser = new User();
            fakeUser.setId(1L);
            fakeUser.setName("Anônimo");
            art.setAuthor(fakeUser);

            artRepository.save(art);

            model.addAttribute("message", "Arte enviada com sucesso!");

            return "redirect:/";

        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("error", "Erro ao enviar arte.");
            return "upload";
        }
    }
}
