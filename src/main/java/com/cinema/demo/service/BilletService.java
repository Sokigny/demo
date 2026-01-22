package com.cinema.demo.service;

import com.cinema.demo.model.Billet;
import com.cinema.demo.model.Reservation;
import com.cinema.demo.model.Seance;
import com.cinema.demo.model.Siege;
import com.cinema.demo.repository.BilletRepository;
import com.cinema.demo.repository.ReservationRepository;
import com.cinema.demo.repository.SeanceRepository;
import com.cinema.demo.repository.SiegeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
