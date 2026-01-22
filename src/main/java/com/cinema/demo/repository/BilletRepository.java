package com.cinema.demo.repository;

import com.cinema.demo.model.Billet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BilletRepository extends JpaRepository<Billet, Integer> {
    
    List<Billet> findByReservationId(Integer reservationId);
    
    List<Billet> findBySiegeId(Integer siegeId);
    
    List<Billet> findBySeanceId(Integer seanceId);
    
    List<Billet> findByStatut(Integer statut);
    
    List<Billet> findBySeanceIdAndSiegeId(Integer seanceId, Integer siegeId);
}
