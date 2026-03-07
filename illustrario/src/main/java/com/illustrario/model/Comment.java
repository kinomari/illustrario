package com.illustrario.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

@Entity
@Table(name = "comments")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "artwork_id", nullable = false)
    private Artwork artwork;

    @NotBlank(message = "O nome é obrigatório")
    @Size(max = 80)
    @Column(name = "author_name", nullable = false, length = 80)
    private String authorName;

    @NotBlank(message = "O comentário não pode estar vazio")
    @Size(max = 500, message = "Máximo de 500 caracteres")
    @Column(nullable = false, length = 500)
    private String content;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private boolean removed = false;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    public Comment() {}

    public Comment(Artwork artwork, String authorName, String content) {
        this.artwork = artwork;
        this.authorName = authorName;
        this.content = content;
    }

    public Long getId() { return id; }

    public Artwork getArtwork() { return artwork; }
    public void setArtwork(Artwork artwork) { this.artwork = artwork; }

    public String getAuthorName() { return authorName; }
    public void setAuthorName(String authorName) { this.authorName = authorName; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public LocalDateTime getCreatedAt() { return createdAt; }

    public boolean isRemoved() { return removed; }
    public void setRemoved(boolean removed) { this.removed = removed; }
}