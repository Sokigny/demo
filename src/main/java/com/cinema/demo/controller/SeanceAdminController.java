package com.cinema.demo.controller;

import com.cinema.demo.model.Billet;
import com.cinema.demo.model.Film;
import com.cinema.demo.model.Seance;
import com.cinema.demo.model.TypePersonne;
import com.cinema.demo.model.TypeSiege;
import com.cinema.demo.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.sql.Date;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin/seances")
public class SeanceAdminController {

    @Autowired
    private SeanceService seanceService;

    @Autowired
    private FilmService filmService;

    @Autowired
    private SalleService salleService;

    @Autowired
    private TypeSiegeService typeSiegeService;

    @Autowired
    private BilletService billetService;

    @Autowired
    private TypePersonneService typePersonneService;

    @GetMapping
    public String listSeances(Model model) {
        List<Seance> seances = seanceService.getAllSeances();

        // Calculer les revenus et capacité pour chaque séance
        Map<Integer, Integer> revenusMap = new java.util.HashMap<>();
        Map<Integer, Integer> capaciteMap = new java.util.HashMap<>();

        for (Seance seance : seances) {
            int revenus = seanceService.getRevenusSeance(seance.getId());
            revenusMap.put(seance.getId(), revenus);

            // Utiliser la capacité de la salle si définie, sinon calculer depuis les sièges
            Integer capacite = seance.getSalle().getCapacite();
            if (capacite == null || capacite == 0) {
                capacite = salleService.getSiegesBySalle(seance.getSalle().getId()).size();
            }
            capaciteMap.put(seance.getId(), capacite);
        }

        model.addAttribute("seances", seances);
        model.addAttribute("revenusMap", revenusMap);
        model.addAttribute("capaciteMap", capaciteMap);
        return "admin/seances/list";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("seance", new Seance());
        model.addAttribute("films", filmService.getAllFilms());
        model.addAttribute("salles", salleService.getAllSalles());
        return "admin/seances/form";
    }

    @PostMapping
    public String createSeance(@ModelAttribute Seance seance, RedirectAttributes redirectAttributes) {
        // Calculer l'heure de fin si non définie (basée sur la durée du film)
        if (seance.getHeureFin() == null && seance.getFilm() != null && seance.getHeureDebut() != null) {
            Film film = filmService.getFilmById(seance.getFilm().getId()).orElse(null);
            if (film != null && film.getDuree() != null) {
                seance.setHeureFin(seance.getHeureDebut().plusMinutes(film.getDuree()));
            }
        }

        // Vérifier le chevauchement
        if (seanceService.hasOverlap(seance)) {
            redirectAttributes.addFlashAttribute("error",
                    "Cette salle est déjà occupée pour cette plage horaire !");
            return "redirect:/admin/seances/new";
        }

        // Créer la séance
        Seance savedSeance = seanceService.createSeance(seance);

        redirectAttributes.addFlashAttribute("success", "Séance créée avec succès!");
        return "redirect:/admin/seances";
    }

    @PostMapping("/{id}/configure-tarifs")
    public String configureTarifs(@PathVariable Integer id,
            @RequestParam List<Integer> typeSiegeIds,
            @RequestParam List<Integer> montants,
            RedirectAttributes redirectAttributes) {
        Seance seance = seanceService.getSeanceById(id).orElse(null);
        if (seance == null) {
            return "redirect:/admin/seances";
        }

        // Valider que tous les montants sont positifs
        for (Integer montant : montants) {
            if (montant == null || montant <= 0) {
                redirectAttributes.addFlashAttribute("error",
                        "Tous les tarifs doivent être supérieurs à 0");
                return "redirect:/admin/seances/" + id + "/configure-tarifs";
            }
        }

        // Enregistrer les tarifs

        redirectAttributes.addFlashAttribute("success", "Séance créée avec succès avec les tarifs!");
        return "redirect:/admin/seances";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Integer id, Model model) {
        Seance seance = seanceService.getSeanceById(id).orElse(null);
        if (seance == null) {
            return "redirect:/admin/seances";
        }
        model.addAttribute("seance", seance);
        model.addAttribute("films", filmService.getAllFilms());
        model.addAttribute("salles", salleService.getAllSalles());
        return "admin/seances/form";
    }

    @PostMapping("/update/{id}")
    public String updateSeance(@PathVariable Integer id, @ModelAttribute Seance seance,
            RedirectAttributes redirectAttributes) {
        seance.setId(id);

        // Calculer l'heure de fin si non définie (basée sur la durée du film)
        if (seance.getHeureFin() == null && seance.getFilm() != null && seance.getHeureDebut() != null) {
            Film film = filmService.getFilmById(seance.getFilm().getId()).orElse(null);
            if (film != null && film.getDuree() != null) {
                seance.setHeureFin(seance.getHeureDebut().plusMinutes(film.getDuree()));
            }
        }

        // Vérifier le chevauchement (exclure la séance actuelle)
        if (seanceService.hasOverlapExcluding(seance, id)) {
            redirectAttributes.addFlashAttribute("error",
                    "Cette salle est déjà occupée pour cette plage horaire !");
            return "redirect:/admin/seances/edit/" + id;
        }

        seanceService.updateSeance(id, seance);
        redirectAttributes.addFlashAttribute("success", "Séance modifiée avec succès!");
        return "redirect:/admin/seances";
    }

    @PostMapping("/{id}/update-tarifs")
    public String updateTarifs(@PathVariable Integer id,
            @RequestParam List<Integer> tarifIds,
            @RequestParam List<Integer> montants,
            RedirectAttributes redirectAttributes) {
        seanceService.updateTarifs(tarifIds, montants);
        redirectAttributes.addFlashAttribute("success", "Tarifs mis à jour avec succès!");
        return "redirect:/admin/seances";
    }

    @GetMapping("/delete/{id}")
    public String deleteSeance(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        seanceService.deleteSeance(id);
        redirectAttributes.addFlashAttribute("success", "Séance supprimée avec succès!");
        return "redirect:/admin/seances";
    }

    @GetMapping("/{id}/billets")
    @ResponseBody
    public List<Map<String, Object>> getBilletsSeance(@PathVariable Integer id) {
        List<Billet> billets = billetService.getBilletsBySeanceId(id);

        return billets.stream().map(billet -> {
            Map<String, Object> billetData = new HashMap<>();
            billetData.put("nomClient", billet.getClient().getNom());
            billetData.put("typePersonne",
                    billet.getPrix() != null && typePersonneService.getTypeDePersonne(Date.from(billet.getClient()
                            .getDateNaissance().atStartOfDay(ZoneId.systemDefault()).toInstant())) != null
                                    ? typePersonneService
                                            .getTypeDePersonne(Date.from(billet.getClient().getDateNaissance()
                                                    .atStartOfDay(ZoneId.systemDefault()).toInstant()))
                                            .orElse(null).getNom()
                                    : null);
            billetData.put("siege", billet.getSiege() != null ? billet.getSiege().getNumero() : null);
            billetData.put("typeSiege", billet.getSiege() != null && billet.getSiege().getTypeSiege() != null
                    ? billet.getSiege().getTypeSiege().getNom()
                    : null);
            billetData.put("prix", billet.getPrix());
            return billetData;
        }).collect(Collectors.toList());
    }

}
