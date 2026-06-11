package com.illustrario.repository;

import com.illustrario.model.Artwork;
import com.illustrario.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByArtworkOrderByCreatedAtDesc(Artwork artwork);
    List<Comment> findAllByOrderByCreatedAtDesc();
    @Query("SELECT c FROM Comment c JOIN FETCH c.artwork ORDER BY c.createdAt DESC")
    List<Comment> findAllWithArtworkOrderByCreatedAtDesc();
    void deleteByArtwork(Artwork artwork);
}
