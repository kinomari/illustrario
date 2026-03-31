package com.illustrario.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Entity
@Table(name = "artworks")
public class Artwork {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "O título é obrigatório")
    @Column(nullable = false)
    private String title;

    @Column(name = "artist_name")
    private String artistName;

    @Column(name = "file_name", nullable = false)
    private String fileName;

    @Column(name = "file_path", nullable = false)
    private String filePath;

    @Column(name = "theme", nullable = false)
    private String theme;

    @Column(name = "uploaded_at", nullable = false)
    private LocalDateTime uploadedAt;

    @Column(length = 500)
    private String description;

    @Column(nullable = false)
    private boolean removed = false;

    // Vínculo com o usuário que enviou a obra
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @PrePersist
    protected void onCreate() { this.uploadedAt = LocalDateTime.now(); }

    public Artwork() {}

    public Artwork(String title, String artistName, String fileName,
                   String filePath, String theme, String description) {
        this.title = title;
        this.artistName = artistName;
        this.fileName = fileName;
        this.filePath = filePath;
        this.theme = theme;
        this.description = description;
    }

    public Long getId() { return id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getArtistName() { return artistName; }
    public void setArtistName(String artistName) { this.artistName = artistName; }
    public String getFileName() { return fileName; }
    public void setFileName(String fileName) { this.fileName = fileName; }
    public String getFilePath() { return filePath; }
    public void setFilePath(String filePath) { this.filePath = filePath; }
    public String getTheme() { return theme; }
    public void setTheme(String theme) { this.theme = theme; }
    public LocalDateTime getUploadedAt() { return uploadedAt; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public boolean isRemoved() { return removed; }
    public void setRemoved(boolean removed) { this.removed = removed; }
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
}