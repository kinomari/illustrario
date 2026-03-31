package com.illustrario.service;

import com.illustrario.dto.ArtworkUploadDto;
import com.illustrario.model.Artwork;
import com.illustrario.model.User;
import com.illustrario.repository.ArtworkRepository;
import com.illustrario.repository.UserRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class ArtworkService {

    private final ArtworkRepository artworkRepository;
    private final ImageStorageService imageStorageService;
    private final UserRepository userRepository;

    public ArtworkService(ArtworkRepository artworkRepository,
                          ImageStorageService imageStorageService,
                          UserRepository userRepository) {
        this.artworkRepository = artworkRepository;
        this.imageStorageService = imageStorageService;
        this.userRepository = userRepository;
    }

    /**
     * Salva a obra vinculada ao usuário logado.
     * @param userEmail e-mail do usuário autenticado (vindo do SecurityContext)
     */
    @Transactional
    public Artwork upload(ArtworkUploadDto dto, String currentTheme,
                          String userEmail) throws IOException {
        String filePath = imageStorageService.save(dto.getImageFile());
        String fileName = dto.getImageFile().getOriginalFilename();

        // Usa o apelido do usuário como nome do artista automaticamente
        User user = userRepository.findByEmail(userEmail)
            .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado"));

        Artwork artwork = new Artwork(
            dto.getTitle(),
            user.getNickname(),   // artistName = apelido do usuário logado
            fileName,
            filePath,
            currentTheme,
            dto.getDescription()
        );
        artwork.setUser(user);

        return artworkRepository.save(artwork);
    }

    public List<Artwork> getRecentArtworks() {
        return artworkRepository.findTop12ByOrderByUploadedAtDesc();
    }

    public List<Artwork> getArtworksByTheme(String theme) {
        return artworkRepository.findByThemeOrderByUploadedAtDesc(theme);
    }

    public List<Artwork> getArtworksByUser(User user) {
        return artworkRepository.findByUserOrderByUploadedAtDesc(user);
    }

    public Optional<Artwork> findById(Long id) {
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