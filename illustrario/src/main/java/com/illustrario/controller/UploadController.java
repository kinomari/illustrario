package com.illustrario.controller;

import com.illustrario.dto.ArtworkUploadDto;
import com.illustrario.service.ArtworkService;
import com.illustrario.service.DailyThemeService;
import jakarta.validation.Valid;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;

@Controller
@RequestMapping("/upload")
public class UploadController {

    private final ArtworkService artworkService;
    private final DailyThemeService dailyThemeService;

    public UploadController(ArtworkService artworkService, DailyThemeService dailyThemeService) {
        this.artworkService = artworkService;
        this.dailyThemeService = dailyThemeService;
    }

    @GetMapping
    public String uploadForm(Model model) {
        model.addAttribute("theme", dailyThemeService.getTodayTheme());
        model.addAttribute("uploadDto", new ArtworkUploadDto());
        return "upload/form";
    }

    @PostMapping
    public String processUpload(@Valid @ModelAttribute("uploadDto") ArtworkUploadDto dto,
                                BindingResult bindingResult,
                                @AuthenticationPrincipal UserDetails userDetails,
                                RedirectAttributes redirectAttributes,
                                Model model) {

        var todayTheme = dailyThemeService.getTodayTheme();

        if (bindingResult.hasErrors()) {
            model.addAttribute("theme", todayTheme);
            return "upload/form";
        }

        try {
            artworkService.upload(dto, todayTheme.getWord(), userDetails.getUsername());
            redirectAttributes.addFlashAttribute("successMessage", "Arte enviada! 🎨");
            return "redirect:/";   // ← vai para a homepage após upload
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        } catch (IOException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Erro ao salvar a imagem.");
        }

        return "redirect:/upload";
    }
}