package com.illustrario.service;

import com.illustrario.model.Artwork;
import com.illustrario.model.Like;
import com.illustrario.model.User;
import com.illustrario.repository.ArtworkRepository;
import com.illustrario.repository.LikeRepository;
import com.illustrario.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class LikeService {

    private final LikeRepository likeRepository;
    private final ArtworkRepository artworkRepository;
    private final UserRepository userRepository;

    public LikeService(LikeRepository likeRepository,
                       ArtworkRepository artworkRepository,
                       UserRepository userRepository) {
        this.likeRepository = likeRepository;
        this.artworkRepository = artworkRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public boolean toggleLike(Long artworkId, String userEmail) {
        Artwork artwork = artworkRepository.findById(artworkId)
            .orElseThrow(() -> new IllegalArgumentException("Obra nao encontrada"));

        User user = userRepository.findByEmail(userEmail)
            .orElseThrow(() -> new IllegalArgumentException("Usuario nao encontrado"));

        if (likeRepository.existsByArtworkAndUser(artwork, user)) {
            likeRepository.deleteByArtworkAndUser(artwork, user);
            return false;
        }

        likeRepository.save(new Like(artwork, user));
        return true;
    }

    public long countLikes(Long artworkId) {
        Artwork artwork = artworkRepository.findById(artworkId)
            .orElseThrow(() -> new IllegalArgumentException("Obra nao encontrada"));
        return likeRepository.countByArtwork(artwork);
    }

    public boolean hasLiked(Long artworkId, String userEmail) {
        User user = userRepository.findByEmail(userEmail).orElse(null);
        if (user == null) {
            return false;
        }

        return artworkRepository.findById(artworkId)
            .map(artwork -> likeRepository.existsByArtworkAndUser(artwork, user))
            .orElse(false);
    }
}
