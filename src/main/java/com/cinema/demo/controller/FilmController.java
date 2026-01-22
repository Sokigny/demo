package com.cinema.demo.controller;

import com.cinema.demo.model.Film;
import com.cinema.demo.service.FilmService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/films")
public class FilmController {

    @Autowired
    private FilmService filmService;

    @GetMapping
    public String listFilms(Model model) {
        model.addAttribute("films", filmService.getAllFilms());
        return "admin/films/list";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("film", new Film());
        return "admin/films/form";
    }

    @PostMapping
    public String createFilm(@ModelAttribute Film film, RedirectAttributes redirectAttributes) {
        filmService.saveFilm(film);
        redirectAttributes.addFlashAttribute("success", "Film ajouté avec succès!");
        return "redirect:/admin/films";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Integer id, Model model) {
        Film film = filmService.getFilmById(id).orElse(null);
        if (film == null) {
            return "redirect:/admin/films";
        }
        model.addAttribute("film", film);
        return "admin/films/form";
    }

    @PostMapping("/update/{id}")
    public String updateFilm(@PathVariable Integer id, @ModelAttribute Film film, RedirectAttributes redirectAttributes) {
        film.setId(id);
        filmService.saveFilm(film);
        redirectAttributes.addFlashAttribute("success", "Film modifié avec succès!");
        return "redirect:/admin/films";
    }

    @GetMapping("/delete/{id}")
    public String deleteFilm(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        filmService.deleteFilm(id);
        redirectAttributes.addFlashAttribute("success", "Film supprimé avec succès!");
        return "redirect:/admin/films";
    }
}
