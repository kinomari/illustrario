package com.illustrario.controller;

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

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping("/{artworkId}")
    public String addComment(@PathVariable Long artworkId,
                             @RequestParam String content,
                             @AuthenticationPrincipal UserDetails userDetails,
                             RedirectAttributes redirectAttributes) {
        try {
            // Usa o e-mail do usuário logado como nome do autor
            String authorName = userDetails.getUsername();
            commentService.addComment(artworkId, authorName, content);
            redirectAttributes.addFlashAttribute("successMessage", "Comentário adicionado!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Erro ao comentar: " + e.getMessage());
        }

        return "redirect:/gallery/artwork/" + artworkId;
    }
}