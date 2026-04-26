package com.illustrario.repository;

import com.illustrario.model.Artwork;
import com.illustrario.model.Like;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LikeRepository extends JpaRepository<Like, Long> {

    long countByArtwork(Artwork artwork);

    boolean existsByArtworkAndVisitorIp(Artwork artwork, String visitorIp);

    void deleteByArtworkAndVisitorIp(Artwork artwork, String visitorIp);
}