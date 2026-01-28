package com.cinema.demo.controller;

import com.cinema.demo.service.PubliciteService;
import com.cinema.demo.service.SocieteService;
import com.cinema.demo.service.SeanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Map;

@Controller
@RequestMapping("/admin/statistiques")
public class StatistiqueController {
    
    @Autowired
    private PubliciteService publiciteService;
    
    @Autowired
    private SocieteService societeService;
    
    @Autowired
    private SeanceService seanceService;
    
    @GetMapping
    public String showStatistiques(Model model) {
        model.addAttribute("societes", societeService.findAll());
        model.addAttribute("seances", seanceService.getAllSeances());
        return "admin/statistiques/ca-publicites";
    }
    
    @GetMapping("/ca-seances")
    public String showCASeances(Model model) {
        model.addAttribute("societes", societeService.findAll());
        model.addAttribute("seances", seanceService.getAllSeances());
        return "admin/statistiques/ca-seances";
    }
    
    @GetMapping("/ca-seances/calculate")
    public String calculateCASeances(
            @RequestParam(required = false) Integer societeId,
            @RequestParam(required = false) Integer seanceId,
            @RequestParam(required = false) Integer mois,
            @RequestParam(required = false) Integer annee,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateDebut,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateFin,
            Model model) {
        
        // Si aucune date n'est spécifiée, utiliser décembre 2025 par défaut
        if (dateDebut == null && dateFin == null && mois == null && annee == null) {
            mois = 12;
            annee = 2025;
        }
        
        // Calculer les dates de début et fin si mois/année sont fournis
        if (mois != null && annee != null && dateDebut == null && dateFin == null) {
            YearMonth yearMonth = YearMonth.of(annee, mois);
            dateDebut = yearMonth.atDay(1);
            dateFin = yearMonth.atEndOfMonth();
        }
        
        // Calculer le CA par séance
        Map<String, Object> statistiques = seanceService.calculateCAParSeance(
            societeId, seanceId, dateDebut, dateFin
        );
        
        model.addAttribute("statistiques", statistiques);
        model.addAttribute("societeId", societeId);
        model.addAttribute("seanceId", seanceId);
        model.addAttribute("mois", mois);
        model.addAttribute("annee", annee);
        model.addAttribute("dateDebut", dateDebut);
        model.addAttribute("dateFin", dateFin);
        model.addAttribute("societes", societeService.findAll());
        model.addAttribute("seances", seanceService.getAllSeances());
        
        return "admin/statistiques/ca-seances";
    }
    
    @GetMapping("/ca-publicites")
    public String calculateCA(
            @RequestParam(required = false) Integer societeId,
            @RequestParam(required = false) Integer seanceId,
            @RequestParam(required = false) Integer mois,
            @RequestParam(required = false) Integer annee,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateDebut,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateFin,
            Model model) {
        
        // Si aucune date n'est spécifiée, utiliser décembre 2025 par défaut
        if (dateDebut == null && dateFin == null && mois == null && annee == null) {
            mois = 12;
            annee = 2025;
        }
        
        // Calculer les dates de début et fin si mois/année sont fournis
        if (mois != null && annee != null && dateDebut == null && dateFin == null) {
            YearMonth yearMonth = YearMonth.of(annee, mois);
            dateDebut = yearMonth.atDay(1);
            dateFin = yearMonth.atEndOfMonth();
        }
        
        // Calculer le CA total et les détails
        Map<String, Object> statistiques = publiciteService.calculateCA(
            societeId, seanceId, dateDebut, dateFin
        );
        
        model.addAttribute("statistiques", statistiques);
        model.addAttribute("societeId", societeId);
        model.addAttribute("seanceId", seanceId);
        model.addAttribute("mois", mois);
        model.addAttribute("annee", annee);
        model.addAttribute("dateDebut", dateDebut);
        model.addAttribute("dateFin", dateFin);
        model.addAttribute("societes", societeService.findAll());
        model.addAttribute("seances", seanceService.getAllSeances());
        
        return "admin/statistiques/ca-publicites";
    }
}
