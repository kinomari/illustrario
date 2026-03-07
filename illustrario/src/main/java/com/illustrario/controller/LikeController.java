package com.illustrario.controller;

import com.illustrario.service.LikeService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
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
            HttpServletRequest request) {

        String ip = getClientIp(request);
        boolean liked = likeService.toggleLike(artworkId, ip);
        long count = likeService.countLikes(artworkId);

        return ResponseEntity.ok(Map.of("liked", liked, "count", count));
    }

    @GetMapping("/{artworkId}")
    public ResponseEntity<Map<String, Object>> getLikes(
            @PathVariable Long artworkId,
            HttpServletRequest request) {

        String ip = getClientIp(request);
        long count = likeService.countLikes(artworkId);
        boolean hasLiked = likeService.hasLiked(artworkId, ip);

        return ResponseEntity.ok(Map.of("count", count, "hasLiked", hasLiked));
    }

    private String getClientIp(HttpServletRequest request) {
        String forwarded = request.getHeader("X-Forwarded-For");
        if (forwarded != null && !forwarded.isBlank()) {
            return forwarded.split(",")[0].trim();
        }
        return request.getRemoteAddr();
    }
}