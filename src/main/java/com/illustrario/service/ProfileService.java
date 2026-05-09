package com.illustrario.service;

import com.illustrario.model.User;
import com.illustrario.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class ProfileService {

    private final UserRepository userRepository;
    private final SupabaseStorageService supabaseStorageService;
    private static final long MAX_SIZE = 5 * 1024 * 1024;

    public ProfileService(UserRepository userRepository, SupabaseStorageService supabaseStorageService) {
        this.userRepository = userRepository;
        this.supabaseStorageService = supabaseStorageService;
    }

    @Transactional
    public void updateAvatar(String userEmail, MultipartFile file) throws IOException {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("Nenhum arquivo de avatar foi enviado.");
        }

        validateAvatar(file);

        User user = userRepository.findByEmail(userEmail)
            .orElseThrow(() -> new IllegalArgumentException("Usuario nao encontrado"));

        if (user.getAvatarPath() != null && !user.getAvatarPath().isBlank()) {
            try {
                supabaseStorageService.deleteByPublicUrl(user.getAvatarPath());
            } catch (Exception ignored) {
            }
        }

        String avatarUrl = supabaseStorageService.saveAvatar(file);
        user.setAvatarPath(avatarUrl);
        userRepository.save(user);
    }

    private void validateAvatar(MultipartFile file) {
        String ext = getExtension(file.getOriginalFilename()).toLowerCase();
        if (!ext.matches("jpg|jpeg|png|webp")) {
            throw new IllegalArgumentException("Formato invalido. Use JPG, PNG ou WEBP.");
        }
        if (file.getSize() > MAX_SIZE) {
            throw new IllegalArgumentException("Imagem muito grande. Maximo: 5MB.");
        }
    }

    private String getExtension(String filename) {
        if (filename == null || !filename.contains(".")) {
            return "jpg";
        }
        return filename.substring(filename.lastIndexOf('.') + 1);
    }
}
