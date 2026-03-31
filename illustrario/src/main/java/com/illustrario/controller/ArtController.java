package com.illustrario.controller;

import com.illustrario.model.Artwork;
import com.illustrario.service.ArtworkService;
import com.illustrario.service.CommentService;
import com.illustrario.service.LikeService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/gallery/artwork")
public class ArtController {

    private final ArtworkService artworkService;
    private final CommentService commentService;
    private final LikeService likeService;

    public ArtController(ArtworkService artworkService,
                         CommentService commentService,
                         LikeService likeService) {
        this.artworkService = artworkService;
        this.commentService = commentService;
        this.likeService = likeService;
    }

    @GetMapping("/{id}")
    public String artworkDetail(@PathVariable Long id,
                                @AuthenticationPrincipal UserDetails userDetails,
                                Model model) {

        Artwork artwork = artworkService.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Obra não encontrada: " + id));

        long likeCount = likeService.countLikes(id);
        boolean hasLiked = userDetails != null &&
            likeService.hasLiked(id, userDetails.getUsername());

        model.addAttribute("artwork", artwork);
        model.addAttribute("comments", commentService.getComments(id));
        model.addAttribute("likeCount", likeCount);
        model.addAttribute("hasLiked", hasLiked);

        return "art_details";
    }
}