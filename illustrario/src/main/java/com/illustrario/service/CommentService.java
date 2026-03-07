package com.illustrario.service;

import com.illustrario.model.Artwork;
import com.illustrario.model.Comment;
import com.illustrario.repository.ArtworkRepository;
import com.illustrario.repository.CommentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final ArtworkRepository artworkRepository;

    public CommentService(CommentRepository commentRepository,
                          ArtworkRepository artworkRepository) {
        this.commentRepository = commentRepository;
        this.artworkRepository = artworkRepository;
    }

    @Transactional
    public Comment addComment(Long artworkId, String authorName, String content) {
        Artwork artwork = artworkRepository.findById(artworkId)
            .orElseThrow(() -> new IllegalArgumentException("Obra não encontrada"));

        return commentRepository.save(new Comment(artwork, authorName, content));
    }

    public List<Comment> getComments(Long artworkId) {
        Artwork artwork = artworkRepository.findById(artworkId)
            .orElseThrow(() -> new IllegalArgumentException("Obra não encontrada"));

        return commentRepository.findByArtworkOrderByCreatedAtDesc(artwork);
    }
}