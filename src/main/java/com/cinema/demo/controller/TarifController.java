package com.cinema.demo.controller;

import com.cinema.demo.model.Tarif;
import com.cinema.demo.service.TarifService;
import com.cinema.demo.service.TypePersonneService;
import com.cinema.demo.service.TypeSiegeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/tarifs")
public class TarifController {

    @Autowired
    private TarifService tarifService;

    @Autowired
    private TypePersonneService typePersonneService;

    @Autowired
    private TypeSiegeService typeSiegeService;

    @GetMapping
    public String listTarifs(Model model) {
        model.addAttribute("tarifs", tarifService.getAllTarifs());
        return "admin/tarifs/list";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("tarif", new Tarif());
        model.addAttribute("typesPersonne", typePersonneService.getAllTypesPersonne());
        model.addAttribute("typesSiege", typeSiegeService.getAllTypesSiege());
        return "admin/tarifs/form";
    }

    @PostMapping
    public String createTarif(@ModelAttribute Tarif tarif, RedirectAttributes redirectAttributes) {
        try {
            tarifService.createTarif(tarif);
            redirectAttributes.addFlashAttribute("success", "Tarif ajouté avec succès!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Erreur lors de l'ajout du tarif: " + e.getMessage());
        }
        return "redirect:/admin/tarifs";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Integer id, Model model, RedirectAttributes redirectAttributes) {
        Tarif tarif = tarifService.getTarifById(id).orElse(null);
        if (tarif == null) {
            redirectAttributes.addFlashAttribute("error", "Tarif non trouvé");
            return "redirect:/admin/tarifs";
        }
        model.addAttribute("tarif", tarif);
        model.addAttribute("typesPersonne", typePersonneService.getAllTypesPersonne());
        model.addAttribute("typesSiege", typeSiegeService.getAllTypesSiege());
        return "admin/tarifs/form";
    }

    @PostMapping("/update/{id}")
    public String updateTarif(@PathVariable Integer id, @ModelAttribute Tarif tarif, RedirectAttributes redirectAttributes) {
        try {
            tarifService.updateTarif(id, tarif);
            redirectAttributes.addFlashAttribute("success", "Tarif modifié avec succès!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Erreur lors de la modification: " + e.getMessage());
        }
        return "redirect:/admin/tarifs";
    }

    @GetMapping("/delete/{id}")
    public String deleteTarif(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        try {
            tarifService.deleteTarif(id);
            redirectAttributes.addFlashAttribute("success", "Tarif supprimé avec succès!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Erreur lors de la suppression: " + e.getMessage());
        }
        return "redirect:/admin/tarifs";
    }
}
