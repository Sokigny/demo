package com.cinema.demo.controller;

import com.cinema.demo.model.Societe;
import com.cinema.demo.service.SocieteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/societes")
public class SocieteController {
    
    @Autowired
    private SocieteService societeService;
    
    @GetMapping
    public String listSocietes(Model model) {
        model.addAttribute("societes", societeService.findAll());
        return "admin/societes/list";
    }
    
    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("societe", new Societe());
        return "admin/societes/form";
    }
    
    @PostMapping
    public String createSociete(@ModelAttribute Societe societe, RedirectAttributes redirectAttributes) {
        societeService.save(societe);
        redirectAttributes.addFlashAttribute("message", "Société créée avec succès");
        return "redirect:/admin/societes";
    }
    
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Integer id, Model model) {
        societeService.findById(id).ifPresent(societe -> model.addAttribute("societe", societe));
        return "admin/societes/form";
    }
    
    @PostMapping("/update/{id}")
    public String updateSociete(@PathVariable Integer id, @ModelAttribute Societe societe, RedirectAttributes redirectAttributes) {
        societe.setId(id);
        societeService.save(societe);
        redirectAttributes.addFlashAttribute("message", "Société modifiée avec succès");
        return "redirect:/admin/societes";
    }
    
    @GetMapping("/delete/{id}")
    public String deleteSociete(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        societeService.deleteById(id);
        redirectAttributes.addFlashAttribute("message", "Société supprimée avec succès");
        return "redirect:/admin/societes";
    }
}
