package com.illustrario.service;

import com.illustrario.model.Artwork;
import com.illustrario.model.Comment;
import com.illustrario.repository.ArtworkRepository;
import com.illustrario.repository.CommentRepository;
import com.illustrario.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final ArtworkRepository artworkRepository;
    private final UserRepository userRepository;

    public CommentService(CommentRepository commentRepository,
                          ArtworkRepository artworkRepository,
                          UserRepository userRepository) {
        this.commentRepository = commentRepository;
        this.artworkRepository = artworkRepository;
        this.userRepository = userRepository;
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

    public Optional<Comment> findById(Long id) { return commentRepository.findById(id); }
    public List<Comment> findAll() { return commentRepository.findAllByOrderByCreatedAtDesc(); }
    public List<Comment> findAllWithArtwork() { return commentRepository.findAllWithArtworkOrderByCreatedAtDesc(); }

    @Transactional
    public void softDeleteByOwner(Long commentId, String userNickname) {
        Comment comment = commentRepository.findById(commentId)
            .orElseThrow(() -> new IllegalArgumentException("Comentário não encontrado"));
        boolean isAuthor = comment.getAuthorName().equals(userNickname);
        boolean isArtworkOwner = comment.getArtwork().getUser() != null &&
            comment.getArtwork().getUser().getNickname().equals(userNickname);
        boolean isCurator = userRepository.findByNickname(userNickname)
            .map(u -> u.getRole().equals("ROLE_CURATOR")).orElse(false);
        if (!isAuthor && !isArtworkOwner && !isCurator) throw new SecurityException("Sem permissão.");
        comment.setRemoved(true);
        commentRepository.save(comment);
    }

    @Transactional
    public void adminRemove(Long id) {
        commentRepository.findById(id).ifPresent(c -> { c.setRemoved(true); commentRepository.save(c); });
    }

    @Transactional
    public void restore(Long id) {
        commentRepository.findById(id).ifPresent(c -> { c.setRemoved(false); commentRepository.save(c); });
    }
}
