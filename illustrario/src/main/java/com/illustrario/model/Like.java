package com.illustrario.model;

import jakarta.persistence.*;

@Entity
@Table(name = "likes",
       uniqueConstraints = @UniqueConstraint(columnNames = {"artwork_id", "visitor_ip"}))
public class Like {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "artwork_id", nullable = false)
    private Artwork artwork;

    @Column(name = "visitor_ip", nullable = false, length = 45)
    private String visitorIp;

    public Like() {}

    public Like(Artwork artwork, String visitorIp) {
        this.artwork = artwork;
        this.visitorIp = visitorIp;
    }


    public Long getId() { return id; }

    public Artwork getArtwork() { return artwork; }
    public void setArtwork(Artwork artwork) { this.artwork = artwork; }

    public String getVisitorIp() { return visitorIp; }
    public void setVisitorIp(String visitorIp) { this.visitorIp = visitorIp; }
}