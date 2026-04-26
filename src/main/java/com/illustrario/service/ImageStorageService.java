package com.illustrario.service;

import net.coobird.thumbnailator.Thumbnails;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.util.List;
import java.util.UUID;

@Service
public class ImageStorageService {

    @Value("${app.upload.dir}")
    private String uploadDir;

    private static final List<String> ALLOWED_EXTENSIONS = List.of("jpg", "jpeg", "png", "webp", "gif");
    private static final long MAX_SIZE_BYTES = 10 * 1024 * 1024;

    public String save(MultipartFile file) throws IOException {
        validateFile(file);

        Path uploadPath = Paths.get(uploadDir);
        Files.createDirectories(uploadPath);

        String extension = getExtension(file.getOriginalFilename());
        String uniqueFileName = UUID.randomUUID() + "." + extension;

        Path filePath = uploadPath.resolve(uniqueFileName);
        file.transferTo(filePath);

        String thumbName = "thumb_" + uniqueFileName;
        Path thumbPath = uploadPath.resolve(thumbName);
        Thumbnails.of(filePath.toFile())
                .size(600, 600)
                .keepAspectRatio(true)
                .toFile(thumbPath.toFile());

        return "/" + uploadDir + "/" + uniqueFileName;
    }

    public void delete(String filePath) {
        try {
            Path path = Paths.get(filePath.substring(1));
            Files.deleteIfExists(path);

            String thumbPath = filePath.replace(
                "/" + uploadDir + "/",
                "/" + uploadDir + "/thumb_"
            );
            Files.deleteIfExists(Paths.get(thumbPath.substring(1)));
        } catch (IOException e) {
            System.err.println("Erro ao deletar arquivo: " + e.getMessage());
        }
    }


    private void validateFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("Nenhuma imagem foi enviada.");
        }

        String extension = getExtension(file.getOriginalFilename());
        if (!ALLOWED_EXTENSIONS.contains(extension.toLowerCase())) {
            throw new IllegalArgumentException(
                "Formato não suportado. Use: " + String.join(", ", ALLOWED_EXTENSIONS)
            );
        }

        if (file.getSize() > MAX_SIZE_BYTES) {
            throw new IllegalArgumentException("Imagem muito grande. Máximo permitido: 10MB.");
        }
    }

    private String getExtension(String filename) {
        if (filename == null || !filename.contains(".")) {
            return "";
        }
        return filename.substring(filename.lastIndexOf('.') + 1);
    }
}