package com.illustrario.controller;

import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.illustrario.service.UserService;
import com.illustrario.model.User;

import org.springframework.ui.Model;
import jakarta.validation.Valid;

@Controller
public class AuthController {
  private final UserService userService;
  public AuthController(UserService userService) { this.userService = userService; }

  @GetMapping("/register")
  public String showRegister(Model m) { m.addAttribute("userForm", new UserForm()); return "register"; }

  @PostMapping("/register")
  public String register(@ModelAttribute @Valid UserForm userForm, BindingResult br, RedirectAttributes ra) {
    if (br.hasErrors()) return "register";
    try {
      User u = new User();
      u.setName(userForm.getName());
      u.setEmail(userForm.getEmail());
      u.setPassword(userForm.getPassword());
      userService.register(u);
    } catch (Exception e) {
      br.rejectValue("email", "error.user", e.getMessage());
      return "register";
    }
    ra.addFlashAttribute("success", "Cadastro efetuado! Faça o login.");
    return "redirect:/login";
  }

  @GetMapping("/login")
  public String login() { return "login"; }
}
