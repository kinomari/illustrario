package com.illustrario.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.illustrario.repository.ArtRepository;
import com.illustrario.repository.CommentRepository;

@Controller
@RequestMapping("/admin")
@PreAuthorize("hasRole('CURATOR')")
public class AdminController {
  private final ArtRepository artRepo;
  private final CommentRepository commentRepo;

  public AdminController(ArtRepository artRepo, CommentRepository commentRepo) {
    this.artRepo = artRepo;
    this.commentRepo = commentRepo;
  }

  @PostMapping("/art/{id}/remove")
  public String removeArt(@PathVariable Long id) {
    artRepo.findById(id).ifPresent(a -> { a.setRemoved(true); artRepo.save(a); });
    return "redirect:/";
  }

  @PostMapping("/comment/{id}/remove")
  public String removeComment(@PathVariable Long id) {
    commentRepo.findById(id).ifPresent(c -> { c.setRemoved(true); commentRepo.save(c); });
    return "redirect:/";
  }
}