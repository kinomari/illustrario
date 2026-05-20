package com.illustrario.controller;

import com.illustrario.repository.UserRepository;
import com.illustrario.service.ArtworkService;
import com.illustrario.service.CommentService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class DeleteController {

    private final ArtworkService artworkService;
    private final CommentService commentService;
    private final UserRepository userRepository;

    public DeleteController(ArtworkService artworkService,
                            CommentService commentService,
                            UserRepository userRepository) {
        this.artworkService = artworkService;
        this.commentService = commentService;
        this.userRepository = userRepository;
    }

    @PostMapping("/artwork/{id}/delete")
    public String deleteArtwork(@PathVariable Long id,
                                @AuthenticationPrincipal UserDetails userDetails,
                                RedirectAttributes ra) {
        try {
            artworkService.softDeleteByOwner(id, userDetails.getUsername());
            ra.addFlashAttribute("successMessage", "Obra ocultada. O curador pode restaurá-la.");
        } catch (SecurityException e) {
            ra.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/profile";
    }

    @PostMapping("/comment/{id}/delete")
    public String deleteComment(@PathVariable Long id,
                                @AuthenticationPrincipal UserDetails userDetails,
                                RedirectAttributes ra) {
        try {
            String nickname = userRepository.findByEmail(userDetails.getUsername())
                .map(u -> u.getNickname()).orElse(userDetails.getUsername());

            Long artworkId = commentService.findById(id)
                .map(c -> c.getArtwork().getId()).orElse(null);

            commentService.softDeleteByOwner(id, nickname);
            ra.addFlashAttribute("successMessage", "Comentário ocultado.");

            if (artworkId != null) return "redirect:/artwork/" + artworkId;
        } catch (SecurityException e) {
            ra.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/";
    }
}