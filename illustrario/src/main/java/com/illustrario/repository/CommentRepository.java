package com.illustrario.repository;

import com.illustrario.model.Artwork;
import com.illustrario.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findByArtworkOrderByCreatedAtDesc(Artwork artwork);
}