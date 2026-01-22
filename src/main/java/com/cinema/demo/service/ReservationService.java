package com.cinema.demo.service;

import com.cinema.demo.model.Client;
import com.cinema.demo.model.Reservation;
import com.cinema.demo.repository.ClientRepository;
import com.cinema.demo.repository.ReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ReservationService {
    
    @Autowired
    private ReservationRepository reservationRepository;
    
    @Autowired
    private ClientRepository clientRepository;
    
    // Create
    public Reservation createReservation(Reservation reservation) {
        if (reservation.getClient() == null || reservation.getClient().getId() == null) {
            throw new RuntimeException("Le client est requis");
        }
        
        Client client = clientRepository.findById(reservation.getClient().getId())
                .orElseThrow(() -> new RuntimeException("Client non trouvé avec l'id: " + reservation.getClient().getId()));

        reservation.setClient(client);
        
        return reservationRepository.save(reservation);
    }
    
    // Read - toutes les réservations
    public List<Reservation> getAllReservations() {
        return reservationRepository.findAll();
    }
    
    // Read - par ID
    public Optional<Reservation> getReservationById(Integer id) {
        return reservationRepository.findById(id);
    }
    
    // Read - par client
    public List<Reservation> getReservationsByClientId(Integer clientId) {
        return reservationRepository.findByClientId(clientId);
    }
    
    // Read - par statut
    public List<Reservation> getReservationsByStatus(Integer status) {
        return reservationRepository.findByStatus(status);
    }
    
    // Update
    public Reservation updateReservation(Integer id, Reservation reservationDetails) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Réservation non trouvée avec l'id: " + id));
        
        if (reservationDetails.getClient() != null && reservationDetails.getClient().getId() != null) {
            Client client = clientRepository.findById(reservationDetails.getClient().getId())
                    .orElseThrow(() -> new RuntimeException("Client non trouvé avec l'id: " + reservationDetails.getClient().getId()));
            reservation.setClient(client);
        }
        
        if (reservationDetails.getBillet() != null) {
            reservation.setBillet(reservationDetails.getBillet());
        }
        
        reservation.setStatus(reservationDetails.getStatus());
        
        return reservationRepository.save(reservation);
    }
    
    // Delete
    public void deleteReservation(Integer id) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Réservation non trouvée avec l'id: " + id));
        reservationRepository.delete(reservation);
    }
}
