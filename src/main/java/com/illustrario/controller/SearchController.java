package com.illustrario.controller;

import com.illustrario.service.ArtworkService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class SearchController {

    private final ArtworkService artworkService;

    public SearchController(ArtworkService artworkService) {
        this.artworkService = artworkService;
    }

    @GetMapping("/search")
    public String search(@RequestParam("q") String query, Model model) {

        var results = artworkService.searchPublicArtworks(query);

        model.addAttribute("query", query);
        model.addAttribute("results", results);

        return "search";
    }
}
