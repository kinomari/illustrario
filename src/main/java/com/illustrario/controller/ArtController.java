package com.illustrario.controller;

import com.illustrario.model.Artwork;
import com.illustrario.model.Comment;
import com.illustrario.model.User;
import com.illustrario.repository.UserRepository;
import com.illustrario.service.ArtworkService;
import com.illustrario.service.CommentService;
import com.illustrario.service.LikeService;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/gallery/artwork")
public class ArtController {

    private final ArtworkService artworkService;
    private final CommentService commentService;
    private final LikeService likeService;
    private final UserRepository userRepository;

    public ArtController(ArtworkService artworkService,
                         CommentService commentService,
                         LikeService likeService,
                         UserRepository userRepository) {
        this.artworkService = artworkService;
        this.commentService = commentService;
        this.likeService = likeService;
        this.userRepository = userRepository;
    }

    @GetMapping("/{id}")
    public String artworkDetail(@PathVariable Long id,
                                @AuthenticationPrincipal UserDetails userDetails,
                                Model model) {

        Artwork artwork = artworkService.findByIdWithUser(id)
            .orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Obra não encontrada: " + id));

        List<Comment> comments = commentService.getComments(id);

        Map<String, User> commentAuthors = comments.stream()
            .map(Comment::getAuthorName)
            .filter(Objects::nonNull)
            .distinct()
            .flatMap(nickname -> userRepository.findByNickname(nickname).stream())
            .filter(u -> u.getNickname() != null)
            .collect(Collectors.toMap(
                User::getNickname,
                u -> u,
                (existing, duplicate) -> existing
            ));

        long likeCount = likeService.countLikes(id);
        boolean hasLiked = userDetails != null &&
            likeService.hasLiked(id, userDetails.getUsername());

        String currentNickname = null;
        if (userDetails != null) {
            currentNickname = userRepository
                .findByEmail(userDetails.getUsername())
                .map(User::getNickname)
                .orElse(null);
        }

        model.addAttribute("artwork", artwork);
        model.addAttribute("comments", comments);
        model.addAttribute("commentAuthors", commentAuthors);
        model.addAttribute("likeCount", likeCount);
        model.addAttribute("hasLiked", hasLiked);
        model.addAttribute("currentNickname", currentNickname);

        return "art_details";
    }
}