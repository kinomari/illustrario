package com.illustrario.repository;

import com.illustrario.model.Artwork;
import com.illustrario.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArtworkRepository extends JpaRepository<Artwork, Long> {

    List<Artwork> findByThemeOrderByUploadedAtDesc(String theme);
    List<Artwork> findTop12ByOrderByUploadedAtDesc();
    long countByTheme(String theme);
    List<Artwork> findByTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCase(
            String title, String description);

    List<Artwork> findByUserOrderByUploadedAtDesc(User user);
}