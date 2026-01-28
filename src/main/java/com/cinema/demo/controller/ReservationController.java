package com.cinema.demo.controller;

import com.cinema.demo.model.*;
import com.cinema.demo.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/reservation")
public class ReservationController {

    @Autowired
    private SeanceService seanceService;

    @Autowired
    private SiegeService siegeService;

    @Autowired
    private BilletService billetService;

    @Autowired
    private ReservationService reservationService;

    @Autowired
    private ClientService clientService;

    @Autowired
    private RemiseService remiseService;

    @Autowired
    private TypeSiegeService typeSiegeService;

    @Autowired
    private TypePersonneService typePersonneService;

    @Autowired
    private TarifService tarifService;

    // Afficher le plan de salle pour une séance
    @GetMapping("/seats/{seanceId}")
    public String showSeatSelection(@PathVariable Integer seanceId, Model model) {
        // Récupérer la séance
        Seance seance = seanceService.getSeanceById(seanceId)
                .orElseThrow(() -> new RuntimeException("Séance non trouvée"));

        // Récupérer tous les sièges de la salle
        List<Siege> allSeats = siegeService.getSiegesBySalleId(seance.getSalle().getId());

        // Récupérer les sièges déjà réservés pour cette séance
        List<Billet> billets = billetService.getBilletsBySeanceId(seanceId);
        Set<Integer> reservedSeatIds = billets.stream()
                .map(b -> b.getSiege().getId())
                .collect(Collectors.toSet());

        // Organiser les sièges par rangée
        Map<String, List<Siege>> seatsByRow = allSeats.stream()
                .collect(Collectors.groupingBy(
                        Siege::getRangee,
                        TreeMap::new,
                        Collectors.toList()));

        // Trier les sièges dans chaque rangée par numéro
        seatsByRow.values().forEach(seats -> seats.sort(Comparator.comparing(Siege::getNumero)));

        // Récupérer les tarifs pour cette séance
        List<TypeSiege> tarifs = typeSiegeService.getAllTypeSieges();
        Map<Integer, Integer> prixParType = tarifs.stream()
                .filter(t -> t.getId() != null && t.getPrix() != null)
                .collect(Collectors.toMap(
                        TypeSiege::getId,
                        TypeSiege::getPrix));

        model.addAttribute("seance", seance);
        model.addAttribute("seatsByRow", seatsByRow);
        model.addAttribute("reservedSeatIds", reservedSeatIds);
        model.addAttribute("prixParType", prixParType);

        return "reservation/seat-selection";
    }

    // Traiter la réservation
    @PostMapping("/confirm")
    @ResponseBody
    public Map<String, Object> confirmReservation(
            @RequestParam Integer seanceId,
            @RequestParam List<Integer> seatIds,
            @RequestParam String clientNom,
            @RequestParam String clientPrenom,
            @RequestParam String clientEmail,
            @RequestParam String clientTelephone,
            @RequestParam String clientDateNaissance) {

        Map<String, Object> response = new HashMap<>();

        try {
            // Vérifier si le client existe déjà par email
            Client client = clientService.getClientByEmail(clientEmail).orElse(null);

            if (client == null) {
                // Créer un nouveau client si non existant
                client = new Client();
                client.setNom(clientNom);
                client.setPrenom(clientPrenom);
                client.setEmail(clientEmail);
                client.setTelephone(clientTelephone);

                // Parser et définir la date de naissance
                if (clientDateNaissance != null && !clientDateNaissance.isEmpty()) {
                    client.setDateNaissance(java.time.LocalDate.parse(clientDateNaissance));
                }

                client = clientService.createClient(client);
            } else {
                // Mettre à jour les informations du client existant si nécessaire
                boolean needUpdate = false;

                if (clientNom != null && !clientNom.equals(client.getNom())) {
                    client.setNom(clientNom);
                    needUpdate = true;
                }
                if (clientPrenom != null && !clientPrenom.equals(client.getPrenom())) {
                    client.setPrenom(clientPrenom);
                    needUpdate = true;
                }
                if (clientTelephone != null && !clientTelephone.equals(client.getTelephone())) {
                    client.setTelephone(clientTelephone);
                    needUpdate = true;
                }
                if (clientDateNaissance != null && !clientDateNaissance.isEmpty()) {
                    java.time.LocalDate dateNaissance = java.time.LocalDate.parse(clientDateNaissance);
                    if (!dateNaissance.equals(client.getDateNaissance())) {
                        client.setDateNaissance(dateNaissance);
                        needUpdate = true;
                    }
                }

                if (needUpdate) {
                    client = clientService.updateClient(client.getId(), client);
                }
            }

            // Calculer l'âge du client
            Integer ageClient = client.getAgeClient();

            // Créer une réservation avec statut 10 (attente)
            Reservation reservation = new Reservation();
            reservation.setClient(client);
            reservation.setDateReservation(LocalDateTime.now());
            reservation.setStatus(10); // Attente
            reservation = reservationService.createReservation(reservation);

            // Récupérer la séance
            Seance seance = seanceService.getSeanceById(seanceId)
                    .orElseThrow(() -> new RuntimeException("Séance non trouvée"));

            // Créer les billets pour chaque siège sélectionné
            int totalPrix = 0;
            List<Billet> billets = new ArrayList<>();

            for (Integer seatId : seatIds) {
                Siege siege = siegeService.getSiegeById(seatId)
                        .orElseThrow(() -> new RuntimeException("Siège non trouvé"));

                // Vérifier si le siège n'est pas déjà réservé
                List<Billet> existingBillets = billetService.getBilletsBySeanceIdAndSiegeId(seanceId, seatId);
                if (!existingBillets.isEmpty()) {
                    throw new RuntimeException(
                            "Le siège " + siege.getRangee() + siege.getNumero() + " est déjà réservé");
                }

                // Récupérer le prix du type de siège
                Integer typeSiegeId = siege.getTypeSiege().getId();
                TypePersonne typePersonne = typePersonneService.getTypeDePersonne(
                        Date.from(client.getDateNaissance().atStartOfDay(ZoneId.systemDefault()).toInstant()))
                        .orElse(null);
                Tarif tarif = tarifService.getTarif(typePersonne, siege.getTypeSiege()).orElse(null);
                // Integer prixOriginal = siege.getTypeSiege().getPrix();
                Integer prixOriginal = tarif.getMontant();

                if (prixOriginal == null) {
                    throw new RuntimeException(
                            "Prix non défini pour le type de siège: " + siege.getTypeSiege().getNom());
                }

                // Chercher la meilleure remise applicable pour ce type de siège et l'âge du
                // client
                Remise remiseApplicable = null;
                Integer prixFinal = prixOriginal;

                if (ageClient != null) {
                    Optional<Remise> meilleureRemise = remiseService.getMeilleureRemiseForTypeSiegeAndAge(typeSiegeId,
                            ageClient);
                    if (meilleureRemise.isPresent()) {
                        remiseApplicable = meilleureRemise.get();
                        prixFinal = remiseApplicable.calculerPrixApresRemise(prixOriginal);
                    }
                }

                // Créer le billet avec le client, le prix final et la remise (si applicable)
                Billet billet = new Billet();
                billet.setReservation(reservation);
                billet.setSiege(siege);
                billet.setSeance(seance);
                billet.setClient(client);
                billet.setPrix(prixFinal);
                billet.setRemise(remiseApplicable); // null si aucune remise
                billet.setStatut(10); // Attente
                billet = billetService.createBillet(billet);
                billets.add(billet);

                totalPrix += prixFinal;
            }

            response.put("success", true);
            response.put("message", "Réservation confirmée !");
            response.put("reservationId", reservation.getId());
            response.put("nombrePlaces", seatIds.size());
            response.put("prixTotal", totalPrix);

        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Erreur: " + e.getMessage());
        }

        return response;
    }

    // API pour rechercher les clients existants (autocomplétion)
    @GetMapping("/api/clients/search")
    @ResponseBody
    public List<Map<String, Object>> searchClients(@RequestParam String query) {
        List<Map<String, Object>> results = new ArrayList<>();

        if (query == null || query.trim().length() < 2) {
            return results;
        }

        // Rechercher par nom, prénom ou email
        List<Client> clientsByNom = clientService.searchByNom(query);
        List<Client> clientsByPrenom = clientService.searchByPrenom(query);

        // Fusionner les résultats sans doublons
        Set<Integer> addedIds = new HashSet<>();

        for (Client client : clientsByNom) {
            if (!addedIds.contains(client.getId())) {
                results.add(clientToMap(client));
                addedIds.add(client.getId());
            }
        }

        for (Client client : clientsByPrenom) {
            if (!addedIds.contains(client.getId())) {
                results.add(clientToMap(client));
                addedIds.add(client.getId());
            }
        }

        // Rechercher aussi par email
        clientService.getClientByEmail(query).ifPresent(client -> {
            if (!addedIds.contains(client.getId())) {
                results.add(clientToMap(client));
            }
        });

        return results;
    }

    private Map<String, Object> clientToMap(Client client) {
        Map<String, Object> map = new HashMap<>();
        map.put("id", client.getId());
        map.put("nom", client.getNom());
        map.put("prenom", client.getPrenom());
        map.put("email", client.getEmail());
        map.put("telephone", client.getTelephone());
        map.put("dateNaissance", client.getDateNaissance() != null ? client.getDateNaissance().toString() : null);
        return map;
    }
}
