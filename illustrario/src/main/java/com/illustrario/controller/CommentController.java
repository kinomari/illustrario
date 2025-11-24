package com.illustrario.controller;

import com.illustrario.model.Art;
import com.illustrario.model.User;
import com.illustrario.service.*;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class CommentController {

    private final CommentService commentService;

    private final ArtService artService;

    private final UserService userService;

    public CommentController(CommentService commentService, ArtService artService, UserService userService) {
        this.commentService = commentService;
        this.artService = artService;
        this.userService = userService;
    }

    @PostMapping("/comment/add/{artId}")
    public String addComment(@PathVariable Long artId,
                             @RequestParam String text,
                             Authentication auth) {

        Art art = artService.getArtById(artId);
        User user = userService.findByEmail(auth.getName());

        commentService.addComment(text, art, user);

        return "redirect:/art/" + artId;
    }
}
