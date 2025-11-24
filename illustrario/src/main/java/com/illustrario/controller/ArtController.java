package com.illustrario.controller;

import com.illustrario.model.Art;
import com.illustrario.model.User;
import com.illustrario.service.ArtService;
import com.illustrario.service.UserService;
import com.illustrario.service.CommentService;
import com.illustrario.service.LikeService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class ArtController {

    private final ArtService artService;

    private final UserService userService;

    private final CommentService commentService;

    private final LikeService likeService;

    public ArtController(ArtService artService, UserService userService, CommentService commentService, LikeService likeService) {
        this.artService = artService;
        this.userService = userService;
        this.commentService = commentService;
        this.likeService = likeService;
    }

    @GetMapping("/arts")
    public String home(Model model) {
        model.addAttribute("arts", artService.getRecentArts());
        return "index";
    }

    @GetMapping("/art/new")
    public String newArt(Model model) {
        model.addAttribute("art", new Art());
        return "art_form";
    }

    @PostMapping("/art/save")
    public String saveArt(@ModelAttribute Art art,
                          @RequestParam("imageFile") MultipartFile imageFile,
                          Authentication auth) throws Exception {

        User user = userService.findByEmail(auth.getName());
        artService.saveArt(art, imageFile, user);

        return "redirect:/";
    }

    @GetMapping("/art/{id}")
    public String artDetails(@PathVariable Long id, Model model) {
    Art art = artService.getArtById(id);

    model.addAttribute("art", art);
    model.addAttribute("comments", commentService.getCommentsForArt(art));
    model.addAttribute("likes", likeService.countLikes(art));

    return "art_details";
}


    @GetMapping("/profile")
    public String profile(Model model, Authentication auth) {
        User user = userService.findByEmail(auth.getName());
        model.addAttribute("arts", artService.getArtsByUser(user));
        return "profile";
    }
}
