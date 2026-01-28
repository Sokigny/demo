package com.cinema.demo.controller;

import com.cinema.demo.model.Publicite;
import com.cinema.demo.service.PubliciteService;
import com.cinema.demo.service.SeanceService;
import com.cinema.demo.service.SocieteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/publicites")
public class PubliciteController {
    
    @Autowired
    private PubliciteService publiciteService;
    
    @Autowired
    private SeanceService seanceService;
    
    @Autowired
    private SocieteService societeService;
    
    @GetMapping
    public String listPublicites(Model model) {
        model.addAttribute("publicites", publiciteService.findAll());
        return "admin/publicites/list";
    }
    
    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("publicite", new Publicite());
        model.addAttribute("seances", seanceService.getAllSeances());
        model.addAttribute("societes", societeService.findAll());
        return "admin/publicites/form";
    }
    
    @PostMapping
    public String createPublicite(@ModelAttribute Publicite publicite, RedirectAttributes redirectAttributes) {
        publiciteService.save(publicite);
        redirectAttributes.addFlashAttribute("message", "Publicité créée avec succès");
        return "redirect:/admin/publicites";
    }
    
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Integer id, Model model) {
        publiciteService.findById(id).ifPresent(publicite -> {
            model.addAttribute("publicite", publicite);
            model.addAttribute("seances", seanceService.getAllSeances());
            model.addAttribute("societes", societeService.findAll());
        });
        return "admin/publicites/form";
    }
    
    @PostMapping("/update/{id}")
    public String updatePublicite(@PathVariable Integer id, @ModelAttribute Publicite publicite, RedirectAttributes redirectAttributes) {
        publicite.setId(id);
        publiciteService.save(publicite);
        redirectAttributes.addFlashAttribute("message", "Publicité modifiée avec succès");
        return "redirect:/admin/publicites";
    }
    
    @GetMapping("/delete/{id}")
    public String deletePublicite(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        publiciteService.deleteById(id);
        redirectAttributes.addFlashAttribute("message", "Publicité supprimée avec succès");
        return "redirect:/admin/publicites";
    }
}
