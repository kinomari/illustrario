package com.illustrario.service;

import com.illustrario.model.*;
import com.illustrario.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class CommentService {

    @Autowired
    private CommentRepository repo;

    public Comment addComment(String text, Art art, User user) {
        Comment c = new Comment();
        c.setText(text);
        c.setArt(art);
        c.setAuthor(user);
        c.setCreatedAt(LocalDateTime.now());
        return repo.save(c);
    }

    public List<Comment> getCommentsForArt(Art art) {
        return repo.findByArtOrderByCreatedAtAsc(art);
    }
}
