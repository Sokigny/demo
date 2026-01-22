package com.cinema.demo.controller;

import com.cinema.demo.model.Billet;
import com.cinema.demo.model.Reservation;
import com.cinema.demo.model.Seance;
import com.cinema.demo.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/seances")
public class SeanceController {
    
    @Autowired
    private SeanceService seanceService;
    
    @Autowired
    private FilmService filmService;
    
    @Autowired
    private SalleService salleService;
    
    @Autowired
    private CinemaService cinemaService;
    
    @Autowired
    private BilletService billetService;
    
    @Autowired
    private ReservationService reservationService;
    
    @GetMapping
    public String listSeances(
            @RequestParam(required = false) String nomFilm,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam(required = false) @DateTimeFormat(pattern = "HH:mm") LocalTime heure,
            @RequestParam(required = false) Integer salleId,
            @RequestParam(required = false) Integer cinemaId,
            Model model) {
        
        // Debug logs
        System.out.println("=== DEBUG RECHERCHE SEANCES ===");
        System.out.println("nomFilm: " + nomFilm);
        System.out.println("date: " + date);
        System.out.println("heure: " + heure);
        System.out.println("salleId: " + salleId);
        System.out.println("cinemaId: " + cinemaId);
        
        List<Seance> seances;
        
        // Recherche multi-critères
        if (nomFilm != null && !nomFilm.trim().isEmpty() && date != null && heure != null) {
            // Recherche par nom de film, date et heure
            System.out.println("Recherche: Film + Date + Heure");
            seances = seanceService.searchSeancesByFilmDateAndTime(nomFilm, date, heure);
            System.out.println("Résultats trouvés: " + seances.size());
            if (seances.isEmpty()) {
                System.out.println("AUCUN RESULTAT - Vérifiez les données:");
                System.out.println("  - Film recherché: '" + nomFilm + "'");
                System.out.println("  - Date: " + date);
                System.out.println("  - Heure: " + heure);
            }
        } else if (nomFilm != null && !nomFilm.trim().isEmpty() && date != null) {
            // Recherche par nom de film et date (sans heure)
            System.out.println("Recherche: Film + Date");
            seances = seanceService.searchSeancesByFilmTitre(nomFilm);
            // Filtrer par date
            seances = seances.stream()
                    .filter(s -> s.getDateSeance().equals(date))
                    .toList();
            System.out.println("Résultats trouvés: " + seances.size());
        } else if (nomFilm != null && !nomFilm.trim().isEmpty()) {
            // Recherche par nom de film uniquement
            System.out.println("Recherche: Film uniquement");
            seances = seanceService.searchSeancesByFilmTitre(nomFilm);
        } else if (date != null && heure != null) {
            // Recherche par date et heure
            System.out.println("Recherche: Date + Heure");
            seances = seanceService.getSeancesAtTime(date, heure);
            System.out.println("Résultats trouvés: " + seances.size());
        } else if (heure != null) {
            // Recherche par heure uniquement (toutes les dates)
            System.out.println("Recherche: Heure uniquement");
            seances = seanceService.getAllSeances();
            // Filtrer par heure
            final LocalTime searchTime = heure;
            seances = seances.stream()
                    .filter(s -> s.getHeureDebut() != null && s.getHeureFin() != null)
                    .filter(s -> !s.getHeureDebut().isAfter(searchTime) && s.getHeureFin().isAfter(searchTime))
                    .toList();
            System.out.println("Résultats trouvés: " + seances.size());
        } else if (date != null) {
            // Recherche par date uniquement
            System.out.println("Recherche: Date uniquement");
            seances = seanceService.getSeancesByDate(date);
        } else if (salleId != null) {
            // Recherche par salle
            System.out.println("Recherche: Salle");
            seances = seanceService.getSeancesBySalleId(salleId);
        } else {
            // Afficher toutes les séances
            System.out.println("Recherche: Toutes les séances");
            seances = seanceService.getAllSeances();
        }
        
        System.out.println("Total avant filtrage: " + seances.size());
        
        // Filtrer par cinéma si spécifié
        if (cinemaId != null) {
            seances = seances.stream()
                    .filter(s -> s.getSalle() != null && 
                                 s.getSalle().getCinema() != null && 
                                 s.getSalle().getCinema().getId().equals(cinemaId))
                    .toList();
        }
        
        // Filtrer par salle si spécifié (et pas déjà filtré)
        if (salleId != null && (date != null || nomFilm != null)) {
            seances = seances.stream()
                    .filter(s -> s.getSalle() != null && s.getSalle().getId().equals(salleId))
                    .toList();
        }
        
        // Calculer les places disponibles pour chaque séance
        java.util.Map<Integer, java.util.Map<String, Integer>> availableSeatsMap = new java.util.HashMap<>();
        java.util.Map<Integer, Integer> revenusMap = new java.util.HashMap<>();
        java.util.Map<Integer, Integer> revenusMaxMap = new java.util.HashMap<>();
        java.util.Map<Integer, Integer> capaciteMap = new java.util.HashMap<>();
        
        int totalCA = 0;
        int totalRevenusMax = 0;
        int totalCapacite = 0;
        
        for (Seance seance : seances) {
            java.util.Map<String, Integer> availableSeats = seanceService.getAvailableSeatsByType(seance.getId());
            availableSeatsMap.put(seance.getId(), availableSeats);
            
            int revenus = seanceService.getRevenusSeance(seance.getId());
            revenusMap.put(seance.getId(), revenus);
            totalCA += revenus;
            
            int revenusMax = seanceService.getRevenusMaxSeance(seance.getId());
            revenusMaxMap.put(seance.getId(), revenusMax);
            totalRevenusMax += revenusMax;
            
            int capacite = seanceService.getCapaciteMaxSeance(seance.getId());
            capaciteMap.put(seance.getId(), capacite);
            totalCapacite += capacite;
        }
        
        model.addAttribute("seances", seances);
        model.addAttribute("availableSeatsMap", availableSeatsMap);
        model.addAttribute("revenusMap", revenusMap);
        model.addAttribute("revenusMaxMap", revenusMaxMap);
        model.addAttribute("capaciteMap", capaciteMap);
        model.addAttribute("totalCA", totalCA);
        model.addAttribute("totalRevenusMax", totalRevenusMax);
        model.addAttribute("totalCapacite", totalCapacite);
        model.addAttribute("films", filmService.getAllFilms());
        model.addAttribute("salles", salleService.getAllSalles());
        model.addAttribute("cinemas", cinemaService.getAllCinemas());
        
        // Garder les valeurs de recherche
        model.addAttribute("nomFilm", nomFilm);
        model.addAttribute("date", date);
        model.addAttribute("heure", heure);
        model.addAttribute("salleId", salleId);
        model.addAttribute("cinemaId", cinemaId);
        
        return "seances/list";
    }
    
    // API pour récupérer les réservations d'une séance (pour le modal)
    @GetMapping("/{seanceId}/reservations")
    @ResponseBody
    public Map<String, Object> getReservationsForSeance(@PathVariable Integer seanceId) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            // Récupérer la séance
            Seance seance = seanceService.getSeanceById(seanceId)
                    .orElseThrow(() -> new RuntimeException("Séance non trouvée"));
            
            // Récupérer tous les billets de la séance
            List<Billet> billets = billetService.getBilletsBySeanceId(seanceId);
            
            // Grouper les billets par réservation
            Map<Integer, List<Billet>> billetsParReservation = billets.stream()
                    .filter(b -> b.getReservation() != null)
                    .collect(Collectors.groupingBy(b -> b.getReservation().getId()));
            
            // Construire la liste des réservations avec leurs billets
            List<Map<String, Object>> reservationsList = new ArrayList<>();
            
            for (Map.Entry<Integer, List<Billet>> entry : billetsParReservation.entrySet()) {
                Map<String, Object> reservationData = new HashMap<>();
                Reservation reservation = entry.getValue().get(0).getReservation();
                
                reservationData.put("id", reservation.getId());
                reservationData.put("dateReservation", reservation.getDateReservation().toString());
                reservationData.put("status", reservation.getStatus());
                reservationData.put("statusLabel", getStatusLabel(reservation.getStatus()));
                
                // Info client
                if (reservation.getClient() != null) {
                    reservationData.put("clientNom", reservation.getClient().getNom());
                    reservationData.put("clientPrenom", reservation.getClient().getPrenom());
                    reservationData.put("clientEmail", reservation.getClient().getEmail());
                }
                
                // Billets de cette réservation
                List<Map<String, Object>> billetsList = new ArrayList<>();
                int totalPrix = 0;
                
                for (Billet billet : entry.getValue()) {
                    Map<String, Object> billetData = new HashMap<>();
                    billetData.put("id", billet.getId());
                    billetData.put("siegeRangee", billet.getSiege().getRangee());
                    billetData.put("siegeNumero", billet.getSiege().getNumero());
                    billetData.put("typeSiege", billet.getSiege().getTypeSiege() != null ? 
                            billet.getSiege().getTypeSiege().getNom() : "N/A");
                    billetData.put("prix", billet.getPrix());
                    billetData.put("statut", getBilletStatusLabel(billet.getStatut()));
                    
                    if (billet.getRemise() != null) {
                        billetData.put("remise", billet.getRemise().getMontant());
                    }
                    
                    billetsList.add(billetData);
                    if (billet.getPrix() != null) {
                        totalPrix += billet.getPrix();
                    }
                }
                
                reservationData.put("billets", billetsList);
                reservationData.put("totalPrix", totalPrix);
                reservationData.put("nombreBillets", billetsList.size());
                
                reservationsList.add(reservationData);
            }
            
            // Trier par date de réservation (plus récent en premier)
            reservationsList.sort((a, b) -> {
                String dateA = (String) a.get("dateReservation");
                String dateB = (String) b.get("dateReservation");
                return dateB.compareTo(dateA);
            });
            
            response.put("success", true);
            response.put("seanceId", seanceId);
            response.put("filmTitre", seance.getFilm().getTitre());
            response.put("reservations", reservationsList);
            response.put("totalReservations", reservationsList.size());
            
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Erreur: " + e.getMessage());
        }
        
        return response;
    }
    
    private String getStatusLabel(Integer status) {
        if (status == null) return "Inconnu";
        switch (status) {
            case 10: return "En attente";
            case 20: return "Confirmée";
            case 30: return "Annulée";
            default: return "Statut " + status;
        }
    }
    
    private String getBilletStatusLabel(Integer status) {
        if (status == null) return "Inconnu";
        switch (status) {
            case 10: return "En attente";
            case 20: return "Confirmé";
            case 30: return "Annulé";
            case 40: return "Utilisé";
            default: return "Statut " + status;
        }
    }
}
