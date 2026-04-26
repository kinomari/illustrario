package com.illustrario.controller;

import com.illustrario.repository.ArtworkRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class SearchController {

    private final ArtworkRepository artRepository;

    public SearchController(ArtworkRepository artRepository) {
        this.artRepository = artRepository;
    }

    @GetMapping("/search")
    public String search(@RequestParam("q") String query, Model model) {

        var results = artRepository
                .findByTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCase(query, query);

        model.addAttribute("query", query);
        model.addAttribute("results", results);

        return "search";
    }
}
