package com.cinema.demo.repository;

import com.cinema.demo.model.Salle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SalleRepository extends JpaRepository<Salle, Integer> {
    
    List<Salle> findByCinemaId(Integer cinemaId);
    
    List<Salle> findByNomContainingIgnoreCase(String nom);
}
