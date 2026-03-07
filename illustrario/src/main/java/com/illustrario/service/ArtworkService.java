package com.illustrario.service;

import com.illustrario.dto.ArtworkUploadDto;
import com.illustrario.model.Artwork;
import com.illustrario.repository.ArtworkRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;

@Service
public class ArtworkService {

    private final ArtworkRepository artworkRepository;
    private final ImageStorageService imageStorageService;

    public ArtworkService(ArtworkRepository artworkRepository,
                          ImageStorageService imageStorageService) {
        this.artworkRepository = artworkRepository;
        this.imageStorageService = imageStorageService;
    }

    @Transactional
    public Artwork upload(ArtworkUploadDto dto, String currentTheme) throws IOException {

        String filePath = imageStorageService.save(dto.getImageFile());
        String fileName = dto.getImageFile().getOriginalFilename();

        Artwork artwork = new Artwork(
            dto.getTitle(),
            dto.getArtistName(),
            fileName,
            filePath,
            currentTheme,
            dto.getDescription()
        );

        return artworkRepository.save(artwork);
    }

    public List<Artwork> getRecentArtworks() {
        return artworkRepository.findTop12ByOrderByUploadedAtDesc();
    }

    public List<Artwork> getArtworksByTheme(String theme) {
        return artworkRepository.findByThemeOrderByUploadedAtDesc(theme);
    }

    public java.util.Optional<Artwork> findById(Long id) {
        return artworkRepository.findById(id);
    }

    @Transactional
    public void delete(Long id) {
        artworkRepository.findById(id).ifPresent(artwork -> {
            imageStorageService.delete(artwork.getFilePath());
            artworkRepository.delete(artwork);
        });
    }
}