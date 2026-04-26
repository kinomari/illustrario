package com.illustrario.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "daily_themes")
public class DailyTheme {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private LocalDate date;

    @Column(nullable = false)
    private String word;

    @Column(length = 300)
    private String hint;

    public DailyTheme() {}

    public DailyTheme(LocalDate date, String word, String hint) {
        this.date = date;
        this.word = word;
        this.hint = hint;
    }

    public Long getId() { return id; }

    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }

    public String getWord() { return word; }
    public void setWord(String word) { this.word = word; }

    public String getHint() { return hint; }
    public void setHint(String hint) { this.hint = hint; }
}