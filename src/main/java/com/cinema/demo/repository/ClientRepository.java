package com.cinema.demo.repository;

import com.cinema.demo.model.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClientRepository extends JpaRepository<Client, Integer> {
    
    Optional<Client> findByEmail(String email);
    
    boolean existsByEmail(String email);
    
    List<Client> findByNomContainingIgnoreCase(String nom);
    
    List<Client> findByPrenomContainingIgnoreCase(String prenom);
}
