package com.illustrario.repository;

import com.illustrario.model.Artwork;
import com.illustrario.model.Like;
import com.illustrario.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LikeRepository extends JpaRepository<Like, Long> {

    long countByArtwork(Artwork artwork);

    boolean existsByArtworkAndUser(Artwork artwork, User user);

    void deleteByArtworkAndUser(Artwork artwork, User user);
    void deleteByArtwork(Artwork artwork);
}
