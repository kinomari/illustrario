package com.illustrario.model;

import jakarta.persistence.*;

@Entity
@Table(name = "likes", uniqueConstraints = @UniqueConstraint(columnNames = {"art_id", "user_id"}))
public class Like {
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
  private Art art;

  @ManyToOne
  private User user;

 public Like() {}

    public Like(Art art, User user) {
        this.art = art;
        this.user = user;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Art getArt() { return art; }
    public void setArt(Art art) { this.art = art; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
}