package com.illustrario.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "comments")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 400)
    private String text;

    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "art_id")
    private Art art;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User author;

    private boolean removed = false;

    public Comment() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getText() { return text; }
    public void setText(String text) { this.text = text; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public Art getArt() { return art; }
    public void setArt(Art art) { this.art = art; }

    public User getAuthor() { return author; }
    public void setAuthor(User author) { this.author = author; }

    public void setRemoved(boolean b) {
        this.removed = b;
    }

        public boolean isRemoved() {
            return this.removed;
    }
}
