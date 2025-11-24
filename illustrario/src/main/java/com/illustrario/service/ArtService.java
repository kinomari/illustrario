package com.illustrario.service;

import com.illustrario.model.Art;
 
import com.illustrario.model.User;
import com.illustrario.repository.ArtRepository;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.*;
import java.util.List;
import java.util.UUID;

@Service
public class ArtService {

    private final ThemeService themeService;

    private static final String UPLOAD_DIR = "uploads/";

    @Autowired
    private ArtRepository repo;
    public ArtService(ThemeService themeService) { this.themeService = themeService;}

    public Art saveArt(Art art, MultipartFile imageFile, User user) throws IOException {

        if (!imageFile.isEmpty()) {

            String extension = imageFile.getOriginalFilename()
                    .substring(imageFile.getOriginalFilename().lastIndexOf("."));

            String filename = UUID.randomUUID().toString() + extension;

            Path uploadPath = Paths.get(UPLOAD_DIR);

            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            Files.copy(imageFile.getInputStream(),
                    uploadPath.resolve(filename),
                    StandardCopyOption.REPLACE_EXISTING);

            art.setImagePath("/uploads/" + filename);
        }

        art.setAuthor(user);
        art.setCreatedAt(LocalDateTime.now());
        var todayTheme = themeService.getTodayTheme();
        // store the DailyTheme entity on the Art (Art.themeOfDay is a DailyTheme)
        art.setThemeOfDay(todayTheme);

        return repo.save(art);
    }

    public List<Art> getRecentArts() {
        return repo.findTop20ByOrderByCreatedAtDesc();
    }

    public List<Art> getArtsByUser(User user) {
        return repo.findByAuthor(user);
    }

    public Art getArtById(Long id) {
        return repo.findById(id).orElse(null);
    }
}
