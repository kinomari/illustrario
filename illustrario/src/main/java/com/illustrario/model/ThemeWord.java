package com.illustrario.model;

import jakarta.persistence.*;

@Entity
@Table(name = "theme_words")
public class ThemeWord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String word;

    @Column(length = 300)
    private String hint;

    @Column(name = "times_used", nullable = false)
    private int timesUsed = 0;

    public ThemeWord() {}

    public ThemeWord(String word, String hint) {
        this.word = word;
        this.hint = hint;
    }

    public Long getId() { return id; }

    public String getWord() { return word; }
    public void setWord(String word) { this.word = word; }

    public String getHint() { return hint; }
    public void setHint(String hint) { this.hint = hint; }

    public int getTimesUsed() { return timesUsed; }
    public void setTimesUsed(int timesUsed) { this.timesUsed = timesUsed; }

    public void incrementTimesUsed() { this.timesUsed++; }
}