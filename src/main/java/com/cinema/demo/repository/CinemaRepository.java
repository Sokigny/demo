package com.cinema.demo.repository;

import com.cinema.demo.model.Cinema;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CinemaRepository extends JpaRepository<Cinema, Integer> {
    
    List<Cinema> findByNomContainingIgnoreCase(String nom);
    
    List<Cinema> findByAdresseContainingIgnoreCase(String adresse);
}
