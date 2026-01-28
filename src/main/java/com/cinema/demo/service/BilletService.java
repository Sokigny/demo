package com.cinema.demo.service;

import com.cinema.demo.model.*;
import com.cinema.demo.repository.BilletRepository;
import com.cinema.demo.repository.ClientRepository;
import com.cinema.demo.repository.ReservationRepository;
import com.cinema.demo.repository.SeanceRepository;
import com.cinema.demo.repository.SiegeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;

@Service
public class BilletService {
    
    @Autowired
    private BilletRepository billetRepository;
    
    @Autowired
    private ReservationRepository reservationRepository;
    
    @Autowired
    private SiegeRepository siegeRepository;
    
    @Autowired
    private SeanceRepository seanceRepository;
    
    @Autowired
    private ClientRepository clientRepository;
    
    @Autowired
    private TypePersonneService typePersonneService;
    
    @Autowired
    private TarifService tarifService;
    
    // Create
    public Billet createBillet(Billet billet) {
        if (billet.getReservation() == null || billet.getReservation().getId() == null) {
            throw new RuntimeException("La réservation est requise");
        }
        
        if (billet.getSiege() == null || billet.getSiege().getId() == null) {
            throw new RuntimeException("Le siège est requis");
        }
        
        if (billet.getSeance() == null || billet.getSeance().getId() == null) {
            throw new RuntimeException("La séance est requise");
        }
        
        Reservation reservation = reservationRepository.findById(billet.getReservation().getId())
                .orElseThrow(() -> new RuntimeException("Réservation non trouvée avec l'id: " + billet.getReservation().getId()));
        
        Siege siege = siegeRepository.findById(billet.getSiege().getId())
                .orElseThrow(() -> new RuntimeException("Siège non trouvé avec l'id: " + billet.getSiege().getId()));
        
        Seance seance = seanceRepository.findById(billet.getSeance().getId())
                .orElseThrow(() -> new RuntimeException("Séance non trouvée avec l'id: " + billet.getSeance().getId()));
        
        billet.setReservation(reservation);
        billet.setSiege(siege);
        billet.setSeance(seance);
        
        return billetRepository.save(billet);
    }
    
    // Read - tous les billets
    public List<Billet> getAllBillets() {
        return billetRepository.findAll();
    }
    
    // Read - par ID
    public Optional<Billet> getBilletById(Integer id) {
        return billetRepository.findById(id);
    }
    
    // Read - par réservation
    public List<Billet> getBilletsByReservationId(Integer reservationId) {
        return billetRepository.findByReservationId(reservationId);
    }
    
    // Read - par siège
    public List<Billet> getBilletsBySiegeId(Integer siegeId) {
        return billetRepository.findBySiegeId(siegeId);
    }
    
    // Read - par séance
    public List<Billet> getBilletsBySeanceId(Integer seanceId) {
        return billetRepository.findBySeanceId(seanceId);
    }
    
    // Read - par statut
    public List<Billet> getBilletsByStatut(Integer statut) {
        return billetRepository.findByStatut(statut);
    }
    
    // Read - par séance et siège
    public List<Billet> getBilletsBySeanceIdAndSiegeId(Integer seanceId, Integer siegeId) {
        return billetRepository.findBySeanceIdAndSiegeId(seanceId, siegeId);
    }
    
    // Calculer le prix du billet selon le client et le siège
    public Integer calculerPrixBillet(Client client, Siege siege) {
        if (client == null || client.getDateNaissance() == null) {
            throw new RuntimeException("Le client doit avoir une date de naissance");
        }
        
        if (siege == null || siege.getTypeSiege() == null) {
            throw new RuntimeException("Le siège doit avoir un type de siège");
        }
        
        // Convertir LocalDate en Date
        java.util.Date dateNaissance = Date.from(client.getDateNaissance().atStartOfDay(ZoneId.systemDefault()).toInstant());
        
        // Trouver le type de personne selon la date de naissance
        Optional<TypePersonne> typePersonneOpt = typePersonneService.getTypeDePersonne(dateNaissance);
        if (!typePersonneOpt.isPresent()) {
            throw new RuntimeException("Aucun type de personne trouvé pour l'âge du client");
        }
        
        TypePersonne typePersonne = typePersonneOpt.get();
        TypeSiege typeSiege = siege.getTypeSiege();
        
        // Trouver le tarif correspondant
        Optional<Tarif> tarifOpt = tarifService.getTarif(typePersonne, typeSiege);
        if (!tarifOpt.isPresent()) {
            throw new RuntimeException("Aucun tarif trouvé pour le type de personne '" + typePersonne.getNom() + 
                                     "' et le type de siège '" + typeSiege.getNom() + "'");
        }
        
        Tarif tarif = tarifOpt.get();
        
        // Utiliser getMontant() qui gère automatiquement le pourcentage
        return tarif.getMontant();
    }
    
    // Créer un billet avec calcul automatique du prix
    public Billet createBilletAvecPrix(Integer reservationId, Integer clientId, Integer siegeId, Integer seanceId, Integer statut) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new RuntimeException("Réservation non trouvée avec l'id: " + reservationId));
        
        Client client = clientRepository.findById(clientId)
                .orElseThrow(() -> new RuntimeException("Client non trouvé avec l'id: " + clientId));
        
        Siege siege = siegeRepository.findById(siegeId)
                .orElseThrow(() -> new RuntimeException("Siège non trouvé avec l'id: " + siegeId));
        
        Seance seance = seanceRepository.findById(seanceId)
                .orElseThrow(() -> new RuntimeException("Séance non trouvée avec l'id: " + seanceId));
        
        // Calculer le prix automatiquement
        Integer prix = calculerPrixBillet(client, siege);
        
        Billet billet = new Billet(reservation, siege, seance, client, prix, statut);
        return billetRepository.save(billet);
    }
    
    // Update
    public Billet updateBillet(Integer id, Billet billetDetails) {
        Billet billet = billetRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Billet non trouvé avec l'id: " + id));
        
        if (billetDetails.getReservation() != null && billetDetails.getReservation().getId() != null) {
            Reservation reservation = reservationRepository.findById(billetDetails.getReservation().getId())
                    .orElseThrow(() -> new RuntimeException("Réservation non trouvée avec l'id: " + billetDetails.getReservation().getId()));
            billet.setReservation(reservation);
        }
        
        if (billetDetails.getSiege() != null && billetDetails.getSiege().getId() != null) {
            Siege siege = siegeRepository.findById(billetDetails.getSiege().getId())
                    .orElseThrow(() -> new RuntimeException("Siège non trouvé avec l'id: " + billetDetails.getSiege().getId()));
            billet.setSiege(siege);
        }
        
        if (billetDetails.getSeance() != null && billetDetails.getSeance().getId() != null) {
            Seance seance = seanceRepository.findById(billetDetails.getSeance().getId())
                    .orElseThrow(() -> new RuntimeException("Séance non trouvée avec l'id: " + billetDetails.getSeance().getId()));
            billet.setSeance(seance);
        }
        
        billet.setStatut(billetDetails.getStatut());
        
        return billetRepository.save(billet);
    }
    
    // Delete
    public void deleteBillet(Integer id) {
        Billet billet = billetRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Billet non trouvé avec l'id: " + id));
        billetRepository.delete(billet);
    }
}
