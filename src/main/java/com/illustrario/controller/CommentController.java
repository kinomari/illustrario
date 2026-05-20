package com.illustrario.controller;

import com.illustrario.repository.UserRepository;
import com.illustrario.service.CommentService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/comments")
public class CommentController {

    private final CommentService commentService;
    private final UserRepository userRepository;

    public CommentController(CommentService commentService,
                             UserRepository userRepository) {
        this.commentService = commentService;
        this.userRepository = userRepository;
    }

    @PostMapping("/{artworkId}")
    public String addComment(@PathVariable Long artworkId,
                             @RequestParam String content,
                             @AuthenticationPrincipal UserDetails userDetails,
                             RedirectAttributes redirectAttributes) {
        try {
            String nickname = userRepository
                .findByEmail(userDetails.getUsername())
                .map(u -> u.getNickname())
                .orElse(userDetails.getUsername());

            commentService.addComment(artworkId, nickname, content);
            redirectAttributes.addFlashAttribute("successMessage", "Comentário adicionado!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Erro: " + e.getMessage());
        }
        return "redirect:/artwork/" + artworkId;
    }
}