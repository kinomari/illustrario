package com.illustrario.controller;

import com.illustrario.model.Comment;
import com.illustrario.service.CommentService;
import jakarta.validation.Valid;
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
                             @RequestParam String authorName,
                             @RequestParam String content,
                             RedirectAttributes redirectAttributes) {
        try {
            commentService.addComment(artworkId, authorName, content);
            redirectAttributes.addFlashAttribute("successMessage", "Comentário adicionado!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Erro ao comentar: " + e.getMessage());
        }

        return "redirect:/gallery/artwork/" + artworkId;
    }
}