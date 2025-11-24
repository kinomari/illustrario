package com.illustrario.model;

import jakarta.persistence.*;
import java.time.*;

@Entity
@Table(name = "daily_themes")
public class DailyTheme {
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false, unique = true)
  private LocalDate date;

  @Column(nullable = false)
  private String theme;

  public Long getId() { return id; }

  public LocalDate getDate() { return date; }
  public void setDate(LocalDate date) { this.date = date; }

  public String getTheme() { return theme; }
  public void setTheme(String theme) { this.theme = theme; }

}
