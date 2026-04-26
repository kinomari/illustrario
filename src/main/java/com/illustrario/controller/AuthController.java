package com.illustrario.controller;

import com.illustrario.dto.UserForm;
import com.illustrario.model.User;
import com.illustrario.service.UserService;
import jakarta.validation.Valid;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/register")
    public String showRegister(Model m) {
        m.addAttribute("userForm", new UserForm());
        return "register";
    }

    @PostMapping("/register")
    public String register(@ModelAttribute("userForm") @Valid UserForm userForm,
                           BindingResult br, RedirectAttributes ra) {
        if (br.hasErrors()) return "register";
        try {
            User u = new User();
            u.setNickname(userForm.getNickname());
            u.setEmail(userForm.getEmail());
            u.setPassword(userForm.getPassword());
            userService.register(u);
        } catch (Exception e) {
            br.rejectValue("email", "error.user", "Este e-mail já está cadastrado.");
            return "register";
        }
        ra.addFlashAttribute("success", "Cadastro efetuado! Faça o login.");
        return "redirect:/login";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/logout")
    public String logoutPage(Authentication authentication) {
        if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
            return "redirect:/login";
        }
        return "logout";
    }
}
