package com.illustrario.repository;

import com.illustrario.model.Artwork;
import com.illustrario.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ArtworkRepository extends JpaRepository<Artwork, Long> {
    List<Artwork> findByThemeOrderByUploadedAtDesc(String theme);
    List<Artwork> findTop12ByOrderByUploadedAtDesc();
    long countByTheme(String theme);
    List<Artwork> findByTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCase(String title, String description);
    Page<Artwork> findByRemovedFalseAndTitleContainingIgnoreCaseOrRemovedFalseAndDescriptionContainingIgnoreCase(
        String title,
        String description,
        Pageable pageable
    );
    List<Artwork> findByUserOrderByUploadedAtDesc(User user);
    Page<Artwork> findByUserAndRemovedFalseOrderByUploadedAtDesc(User user, Pageable pageable);
    List<Artwork> findAllByOrderByUploadedAtDesc();
    List<Artwork> findTop12ByThemeAndRemovedFalseOrderByUploadedAtDesc(String theme);
    Page<Artwork> findByThemeAndRemovedFalseOrderByUploadedAtDesc(String theme, Pageable pageable);
    Page<Artwork> findByRemovedFalseOrderByUploadedAtDesc(Pageable pageable);

    @Query("SELECT a FROM Artwork a LEFT JOIN FETCH a.user WHERE a.id = :id")
    Optional<Artwork> findByIdWithUser(@Param("id") Long id);

    @Query("SELECT a FROM Artwork a LEFT JOIN FETCH a.user ORDER BY a.uploadedAt DESC")
    List<Artwork> findAllWithUserOrderByUploadedAtDesc();
}