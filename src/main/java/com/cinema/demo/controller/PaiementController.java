package com.cinema.demo.controller;

import com.cinema.demo.model.Paiement;
import com.cinema.demo.model.PaiementDetail;
import com.cinema.demo.service.PaiementService;
import com.cinema.demo.service.PaiementDetailService;
import com.cinema.demo.service.SocieteService;
import com.cinema.demo.service.SeanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Map;

@Controller
@RequestMapping("/admin/paiements")
public class PaiementController {

    @Autowired
    private PaiementService paiementService;

    @Autowired
    private PaiementDetailService paiementDetailService;

    @Autowired
    private SocieteService societeService;

    @Autowired
    private SeanceService seanceService;

    @GetMapping
    public String listPaiements(Model model) {
        model.addAttribute("paiements", paiementService.getAllPaiements());
        return "admin/paiements/list";
    }

    @GetMapping("/new")
    public String newPaiement(Model model) {
        model.addAttribute("paiement", new Paiement());
        model.addAttribute("societes", societeService.findAll());
        return "admin/paiements/form";
    }

    @PostMapping("/create")
    public String createPaiement(@ModelAttribute Paiement paiement,
            RedirectAttributes redirectAttributes) {
        try {
            // Créer le paiement avec répartition automatique
            Map<String, Object> result = paiementService.createPaiementAvecRepartition(paiement);
            
            Paiement savedPaiement = (Paiement) result.get("paiement");
            redirectAttributes.addFlashAttribute("success", "Paiement créé avec succès!");
            redirectAttributes.addFlashAttribute("repartitionDetails", result.get("details"));
            return "redirect:/admin/paiements/detail/" + savedPaiement.getId();
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/admin/paiements/new";
        }
    }
    
    @GetMapping("/detail/{id}")
    public String detailPaiement(@PathVariable Integer id, Model model) {
        Paiement paiement = paiementService.getPaiementById(id)
                .orElseThrow(() -> new RuntimeException("Paiement non trouvé"));
        model.addAttribute("paiement", paiement);
        model.addAttribute("details", paiementDetailService.getDetailsByPaiement(id));
        return "admin/paiements/detail";
    }

    @GetMapping("/edit/{id}")
    public String editPaiement(@PathVariable Integer id, Model model) {
        Paiement paiement = paiementService.getPaiementById(id)
                .orElseThrow(() -> new RuntimeException("Paiement non trouvé"));
        model.addAttribute("paiement", paiement);
        model.addAttribute("societes", societeService.findAll());
        return "admin/paiements/form";
    }

    @PostMapping("/update/{id}")
    public String updatePaiement(@PathVariable Integer id,
            @ModelAttribute Paiement paiement,
            RedirectAttributes redirectAttributes) {
        paiement.setId(id);
        // Note: La modification ne recalcule pas la répartition automatiquement
        paiementService.savePaiement(paiement);
        redirectAttributes.addFlashAttribute("success", "Paiement modifié avec succès!");
        redirectAttributes.addFlashAttribute("warning", "Attention: La répartition n'a pas été recalculée.");
        return "redirect:/admin/paiements";
    }

    @GetMapping("/delete/{id}")
    public String deletePaiement(@PathVariable Integer id,
            RedirectAttributes redirectAttributes) {
        paiementService.deletePaiement(id);
        redirectAttributes.addFlashAttribute("success", "Paiement supprimé avec succès!");
        return "redirect:/admin/paiements";
    }
}
