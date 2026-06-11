package com.illustrario.controller;

import com.illustrario.repository.UserRepository;
import com.illustrario.service.ArtworkService;
import com.illustrario.service.CommentService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin")
@PreAuthorize("hasRole('CURATOR')")
public class AdminController {

    private final ArtworkService artworkService;
    private final CommentService commentService;
    private final UserRepository userRepository;

    public AdminController(ArtworkService artworkService,
                           CommentService commentService,
                           UserRepository userRepository) {
        this.artworkService = artworkService;
        this.commentService = commentService;
        this.userRepository = userRepository;
    }

    @GetMapping
    public String dashboard(Model model) {
        var allArtworks  = artworkService.findAllWithUser();
        var allComments  = commentService.findAll();
        var allUsers     = userRepository.findAll();

        long artworksOk      = allArtworks.stream().filter(a -> !a.isRemoved()).count();
        long artworksRemoved = allArtworks.stream().filter(a ->  a.isRemoved()).count();
        long commentsOk      = allComments.stream().filter(c -> !c.isRemoved()).count();
        long commentsRemoved = allComments.stream().filter(c ->  c.isRemoved()).count();

        model.addAttribute("artworksOk",      artworksOk);
        model.addAttribute("artworksRemoved", artworksRemoved);
        model.addAttribute("commentsOk",      commentsOk);
        model.addAttribute("commentsRemoved", commentsRemoved);
        model.addAttribute("totalUsers",      allUsers.size());

        return "admin/dashboard";
    }

    @GetMapping("/artworks")
    public String artworks(@RequestParam(defaultValue = "all") String filter, Model model) {
        var all = artworkService.findAllWithUser();
        var list = switch (filter) {
            case "removed" -> all.stream().filter(a ->  a.isRemoved()).toList();
            case "active"  -> all.stream().filter(a -> !a.isRemoved()).toList();
            default        -> all;
        };
        model.addAttribute("artworks", list);
        model.addAttribute("filter", filter);
        return "admin/artworks";
    }

    @GetMapping("/comments")
    public String comments(@RequestParam(defaultValue = "all") String filter, Model model) {
        var all = commentService.findAllWithArtwork();
        var list = switch (filter) {
            case "removed" -> all.stream().filter(c ->  c.isRemoved()).toList();
            case "active"  -> all.stream().filter(c -> !c.isRemoved()).toList();
            default        -> all;
        };
        model.addAttribute("comments", list);
        model.addAttribute("filter", filter);
        return "admin/comments";
    }

    @GetMapping("/users")
    public String users(Model model) {
        model.addAttribute("users", userRepository.findAll());
        return "admin/users";
    }

    @PostMapping("/artwork/{id}/remove")
    public String removeArtwork(@PathVariable Long id, RedirectAttributes ra) {
        artworkService.softDeleteByOwnerAdmin(id);
        ra.addFlashAttribute("successMessage", "Arte ocultada.");
        return "redirect:/admin/artworks";
    }

    @PostMapping("/artwork/{id}/restore")
    public String restoreArtwork(@PathVariable Long id, RedirectAttributes ra) {
        artworkService.restore(id);
        ra.addFlashAttribute("successMessage", "Arte restaurada.");
        return "redirect:/admin/artworks";
    }

    @PostMapping("/artwork/{id}/hard-delete")
    public String hardDeleteArtwork(@PathVariable Long id, RedirectAttributes ra) {
        artworkService.hardDelete(id);
        ra.addFlashAttribute("successMessage", "Arte permanentemente removida.");
        return "redirect:/admin/artworks";
    }

    @PostMapping("/comment/{id}/remove")
    public String removeComment(@PathVariable Long id, RedirectAttributes ra) {
        commentService.adminRemove(id);
        ra.addFlashAttribute("successMessage", "Comentário ocultado.");
        return "redirect:/admin/comments";
    }

    @PostMapping("/comment/{id}/restore")
    public String restoreComment(@PathVariable Long id, RedirectAttributes ra) {
        commentService.restore(id);
        ra.addFlashAttribute("successMessage", "Comentário restaurado.");
        return "redirect:/admin/comments";
    }

    @PostMapping("/user/{id}/promote")
    public String promoteUser(@PathVariable Long id, RedirectAttributes ra) {
        userRepository.findById(id).ifPresent(u -> {
            u.setRole("ROLE_CURATOR");
            userRepository.save(u);
        });
        ra.addFlashAttribute("successMessage", "Usuário promovido a curador.");
        return "redirect:/admin/users";
    }

    @PostMapping("/user/{id}/demote")
    public String demoteUser(@PathVariable Long id, RedirectAttributes ra) {
        userRepository.findById(id).ifPresent(u -> {
            u.setRole("ROLE_USER");
            userRepository.save(u);
        });
        ra.addFlashAttribute("successMessage", "Usuário rebaixado.");
        return "redirect:/admin/users";
    }
}
