package com.illustrario.controller;

import com.illustrario.service.LikeService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/likes")
public class LikeController {

    private final LikeService likeService;

    public LikeController(LikeService likeService) {
        this.likeService = likeService;
    }

    @PostMapping("/{artworkId}")
    public ResponseEntity<Map<String, Object>> toggleLike(
            @PathVariable Long artworkId,
            @AuthenticationPrincipal UserDetails userDetails) {

        String userEmail = userDetails.getUsername();
        boolean liked = likeService.toggleLike(artworkId, userEmail);
        long count = likeService.countLikes(artworkId);

        return ResponseEntity.ok(Map.of("liked", liked, "count", count));
    }

    @GetMapping("/{artworkId}")
    public ResponseEntity<Map<String, Object>> getLikes(
            @PathVariable Long artworkId,
            @AuthenticationPrincipal UserDetails userDetails) {

        long count = likeService.countLikes(artworkId);
        boolean hasLiked = userDetails != null &&
            likeService.hasLiked(artworkId, userDetails.getUsername());

        return ResponseEntity.ok(Map.of("count", count, "hasLiked", hasLiked));
    }
}