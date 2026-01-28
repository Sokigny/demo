package com.cinema.demo.repository;

import com.cinema.demo.model.TypePersonne;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TypePersonneRepository extends JpaRepository<TypePersonne, Integer> {
    
    Optional<TypePersonne> findByNom(String nom);
    
    List<TypePersonne> findByNomContainingIgnoreCase(String nom);
}
