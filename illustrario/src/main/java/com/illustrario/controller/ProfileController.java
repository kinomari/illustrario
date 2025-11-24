package com.illustrario.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ProfileController {

    @GetMapping("/profile/sample")
    public String profilePage(Model model) {
        model.addAttribute("username", "Usuário Exemplo");
        return "profile";
    }
}
