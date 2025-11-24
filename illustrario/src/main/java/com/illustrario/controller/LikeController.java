package com.illustrario.controller;

import com.illustrario.model.Art;
import com.illustrario.model.User;
import com.illustrario.service.*;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class LikeController {

    private final LikeService likeService;

    private final ArtService artService;

    private final UserService userService;

    public LikeController(LikeService likeService, ArtService artService, UserService userService) {
        this.likeService = likeService;
        this.artService = artService;
        this.userService = userService;
    }

    @PostMapping("/like/{artId}")
    public String like(@PathVariable Long artId, Authentication auth) {

        Art art = artService.getArtById(artId);
        User user = userService.findByEmail(auth.getName());

        likeService.toggleLike(art, user);

        return "redirect:/art/" + artId;
    }
}
