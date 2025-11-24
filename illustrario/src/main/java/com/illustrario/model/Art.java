package com.illustrario.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "arts")
public class Art {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Column(length = 300)
    private String description;

    private String imagePath;

    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "daily_theme_id")
    private DailyTheme themeOfDay;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User author;

    private boolean removed = false;

    public Art() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getImagePath() { return imagePath; }
    public void setImagePath(String imagePath) { this.imagePath = imagePath; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public DailyTheme getThemeOfDay() { return themeOfDay; }
    public void setThemeOfDay(DailyTheme themeOfDay) { this.themeOfDay = themeOfDay; }

    public User getAuthor() { return author; }
    public void setAuthor(User author) { this.author = author; }

    public void setRemoved(boolean b) {
        this.removed = b;
    }

    public boolean isRemoved() {
        return this.removed;
    }
}
