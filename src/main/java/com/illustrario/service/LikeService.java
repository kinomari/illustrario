package com.illustrario.service;

import com.illustrario.model.Artwork;
import com.illustrario.model.Like;
import com.illustrario.repository.ArtworkRepository;
import com.illustrario.repository.LikeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class LikeService {

    private final LikeRepository likeRepository;
    private final ArtworkRepository artworkRepository;

    public LikeService(LikeRepository likeRepository,
                       ArtworkRepository artworkRepository) {
        this.likeRepository = likeRepository;
        this.artworkRepository = artworkRepository;
    }

    @Transactional
    public boolean toggleLike(Long artworkId, String visitorIp) {
        Artwork artwork = artworkRepository.findById(artworkId)
            .orElseThrow(() -> new IllegalArgumentException("Obra não encontrada"));

        if (likeRepository.existsByArtworkAndVisitorIp(artwork, visitorIp)) {
            likeRepository.deleteByArtworkAndVisitorIp(artwork, visitorIp);
            return false;
        } else {
            likeRepository.save(new Like(artwork, visitorIp));
            return true;
        }
    }

    public long countLikes(Long artworkId) {
        Artwork artwork = artworkRepository.findById(artworkId)
            .orElseThrow(() -> new IllegalArgumentException("Obra não encontrada"));
        return likeRepository.countByArtwork(artwork);
    }

    public boolean hasLiked(Long artworkId, String visitorIp) {
        return artworkRepository.findById(artworkId)
            .map(artwork -> likeRepository.existsByArtworkAndVisitorIp(artwork, visitorIp))
            .orElse(false);
    }
}