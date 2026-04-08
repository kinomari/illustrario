package com.illustrario.service;

import com.illustrario.model.User;
import com.illustrario.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.util.UUID;

@Service
public class ProfileService {

    private final UserRepository userRepository;
    private static final String AVATAR_DIR = "uploads/avatars";
    private static final long MAX_SIZE = 5 * 1024 * 1024;

    public ProfileService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    public void updateAvatar(String userEmail, MultipartFile file) throws IOException {
        if (file == null || file.isEmpty()) return;

        validateAvatar(file);

        User user = userRepository.findByEmail(userEmail)
            .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado"));

        Path dir = Paths.get(AVATAR_DIR);
        Files.createDirectories(dir);

        if (user.getAvatarPath() != null) {
            try { Files.deleteIfExists(Paths.get(user.getAvatarPath().substring(1))); }
            catch (IOException ignored) {}
        }

        String ext = getExtension(file.getOriginalFilename());
        String fileName = UUID.randomUUID() + "." + ext;
        Path dest = dir.resolve(fileName);
        file.transferTo(dest);

        user.setAvatarPath("/" + AVATAR_DIR + "/" + fileName);
        userRepository.save(user);
    }

    private void validateAvatar(MultipartFile file) {
        String ext = getExtension(file.getOriginalFilename()).toLowerCase();
        if (!ext.matches("jpg|jpeg|png|webp")) {
            throw new IllegalArgumentException("Formato inválido. Use JPG, PNG ou WEBP.");
        }
        if (file.getSize() > MAX_SIZE) {
            throw new IllegalArgumentException("Imagem muito grande. Máximo: 5MB.");
        }
    }

    private String getExtension(String filename) {
        if (filename == null || !filename.contains(".")) return "jpg";
        return filename.substring(filename.lastIndexOf('.') + 1);
    }
}