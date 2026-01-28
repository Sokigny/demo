package com.cinema.demo.controller;

import com.cinema.demo.model.TypePersonne;
import com.cinema.demo.service.TypePersonneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/types-personne")
public class TypePersonneController {

    @Autowired
    private TypePersonneService typePersonneService;

    @GetMapping
    public String listTypesPersonne(Model model) {
        model.addAttribute("typesPersonne", typePersonneService.getAllTypesPersonne());
        return "admin/types-personne/list";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("typePersonne", new TypePersonne());
        return "admin/types-personne/form";
    }

    @PostMapping
    public String createTypePersonne(@ModelAttribute TypePersonne typePersonne, RedirectAttributes redirectAttributes) {
        try {
            typePersonneService.createTypePersonne(typePersonne);
            redirectAttributes.addFlashAttribute("success", "Type de personne ajouté avec succès!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Erreur lors de l'ajout: " + e.getMessage());
        }
        return "redirect:/admin/types-personne";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Integer id, Model model, RedirectAttributes redirectAttributes) {
        TypePersonne typePersonne = typePersonneService.getTypePersonneById(id).orElse(null);
        if (typePersonne == null) {
            redirectAttributes.addFlashAttribute("error", "Type de personne non trouvé");
            return "redirect:/admin/types-personne";
        }
        model.addAttribute("typePersonne", typePersonne);
        return "admin/types-personne/form";
    }

    @PostMapping("/update/{id}")
    public String updateTypePersonne(@PathVariable Integer id, @ModelAttribute TypePersonne typePersonne, RedirectAttributes redirectAttributes) {
        try {
            typePersonneService.updateTypePersonne(id, typePersonne);
            redirectAttributes.addFlashAttribute("success", "Type de personne modifié avec succès!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Erreur lors de la modification: " + e.getMessage());
        }
        return "redirect:/admin/types-personne";
    }

    @GetMapping("/delete/{id}")
    public String deleteTypePersonne(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        try {
            typePersonneService.deleteTypePersonne(id);
            redirectAttributes.addFlashAttribute("success", "Type de personne supprimé avec succès!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Erreur lors de la suppression: " + e.getMessage());
        }
        return "redirect:/admin/types-personne";
    }
}
