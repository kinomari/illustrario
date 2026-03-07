package com.illustrario.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.web.multipart.MultipartFile;

public class ArtworkUploadDto {

    @NotBlank(message = "O título é obrigatório")
    @Size(max = 100, message = "Título deve ter no máximo 100 caracteres")
    private String title;

    @Size(max = 80, message = "Nome do artista deve ter no máximo 80 caracteres")
    private String artistName;

    @Size(max = 500, message = "Descrição deve ter no máximo 500 caracteres")
    private String description;

    private MultipartFile imageFile;

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getArtistName() { return artistName; }
    public void setArtistName(String artistName) { this.artistName = artistName; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public MultipartFile getImageFile() { return imageFile; }
    public void setImageFile(MultipartFile imageFile) { this.imageFile = imageFile; }
}