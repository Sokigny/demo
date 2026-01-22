package com.cinema.demo.controller;

import com.cinema.demo.model.Salle;
import com.cinema.demo.model.Siege;
import com.cinema.demo.service.SalleService;
import com.cinema.demo.service.CinemaService;
import com.cinema.demo.service.TypeSiegeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/admin/salles")
public class SalleController {

    @Autowired
    private SalleService salleService;

    @Autowired
    private CinemaService cinemaService;
    
    @Autowired
    private TypeSiegeService typeSiegeService;

    @GetMapping
    public String listSalles(Model model) {
        model.addAttribute("salles", salleService.getAllSalles());
        return "admin/salles/list";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("salle", new Salle());
        model.addAttribute("cinemas", cinemaService.getAllCinemas());
        model.addAttribute("typeSieges", typeSiegeService.getAllTypeSieges());
        return "admin/salles/form";
    }

    @PostMapping
    public String createSalle(@ModelAttribute Salle salle, RedirectAttributes redirectAttributes) {
        // Étape 1 : Créer la salle sans les sièges
        Salle savedSalle = salleService.saveSalle(salle);
        
        // Rediriger vers l'étape 2 pour configurer les sièges
        return "redirect:/admin/salles/" + savedSalle.getId() + "/configure-sieges";
    }
    
    @GetMapping("/{id}/configure-sieges")
    public String showConfigureSieges(@PathVariable Integer id, Model model) {
        Salle salle = salleService.getSalleById(id).orElse(null);
        if (salle == null) {
            return "redirect:/admin/salles";
        }
        
        model.addAttribute("salle", salle);
        model.addAttribute("typeSieges", typeSiegeService.getAllTypeSieges());
        return "admin/salles/configure-sieges";
    }
    
    @PostMapping("/{id}/configure-sieges")
    public String configureSieges(@PathVariable Integer id,
                                   @RequestParam List<Integer> typeSiegeIds,
                                   @RequestParam List<Integer> nombreSieges,
                                   RedirectAttributes redirectAttributes) {
        Salle salle = salleService.getSalleById(id).orElse(null);
        if (salle == null) {
            return "redirect:/admin/salles";
        }
        
        // Valider que la somme correspond à la capacité
        int total = nombreSieges.stream().mapToInt(Integer::intValue).sum();
        if (total != salle.getCapacite()) {
            redirectAttributes.addFlashAttribute("error", 
                "La somme des sièges (" + total + ") ne correspond pas à la capacité (" + salle.getCapacite() + ")");
            return "redirect:/admin/salles/" + id + "/configure-sieges";
        }
        
        // Générer les sièges avec les types spécifiés
        salleService.genererSiegesAvecTypes(salle, typeSiegeIds, nombreSieges);
        
        redirectAttributes.addFlashAttribute("success", "Salle créée avec succès avec " + total + " sièges!");
        return "redirect:/admin/salles";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Integer id, Model model) {
        Salle salle = salleService.getSalleById(id).orElse(null);
        if (salle == null) {
            return "redirect:/admin/salles";
        }
        model.addAttribute("salle", salle);
        model.addAttribute("cinemas", cinemaService.getAllCinemas());
        model.addAttribute("sieges", salleService.getSiegesBySalle(id));
        return "admin/salles/form";
    }

    @PostMapping("/update/{id}")
    public String updateSalle(@PathVariable Integer id, @ModelAttribute Salle salle, RedirectAttributes redirectAttributes) {
        salle.setId(id);
        salleService.saveSalle(salle);
        redirectAttributes.addFlashAttribute("success", "Salle modifiée avec succès!");
        return "redirect:/admin/salles";
    }

    @GetMapping("/delete/{id}")
    public String deleteSalle(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        // salleService.deleteSalle(id);
        redirectAttributes.addFlashAttribute("success", "Salle supprimée avec succès!");
        return "redirect:/admin/salles";
    }
    
    @GetMapping("/{id}/sieges")
    public String viewSieges(@PathVariable Integer id, Model model) {
        Salle salle = salleService.getSalleById(id).orElse(null);
        if (salle == null) {
            return "redirect:/admin/salles";
        }
        
        List<Siege> sieges = salleService.getSiegesBySalle(id);
        
        // Grouper les sièges par rangée
        Map<String, List<Siege>> siegesParRangee = new java.util.TreeMap<>();
        for (Siege siege : sieges) {
            siegesParRangee.computeIfAbsent(siege.getRangee(), k -> new java.util.ArrayList<>()).add(siege);
        }
        
        // Trier les sièges par numéro dans chaque rangée
        siegesParRangee.values().forEach(list -> 
            list.sort((s1, s2) -> s1.getNumero().compareTo(s2.getNumero()))
        );
        
        model.addAttribute("salle", salle);
        model.addAttribute("sieges", sieges);
        model.addAttribute("siegesParRangee", siegesParRangee);
        return "admin/salles/sieges";
    }
}
