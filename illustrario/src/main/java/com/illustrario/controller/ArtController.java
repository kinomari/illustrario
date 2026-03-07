package com.illustrario.controller;

import com.illustrario.model.Artwork;
import com.illustrario.service.ArtworkService;
import com.illustrario.service.CommentService;
import com.illustrario.service.LikeService;
import jakarta.servlet.http.HttpServletRequest;
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
                                HttpServletRequest request,
                                Model model) {

        Artwork artwork = artworkService.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Obra não encontrada: " + id));

        String visitorIp = getClientIp(request);

        model.addAttribute("artwork", artwork);
        model.addAttribute("comments", commentService.getComments(id));
        model.addAttribute("likeCount", likeService.countLikes(id));
        model.addAttribute("hasLiked", likeService.hasLiked(id, visitorIp));

        return "gallery/artwork-detail";
    }

    private String getClientIp(HttpServletRequest request) {
        String forwarded = request.getHeader("X-Forwarded-For");
        if (forwarded != null && !forwarded.isBlank()) {
            return forwarded.split(",")[0].trim();
        }
        return request.getRemoteAddr();
    }
}