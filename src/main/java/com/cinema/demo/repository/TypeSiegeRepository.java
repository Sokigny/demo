package com.cinema.demo.repository;

import com.cinema.demo.model.TypeSiege;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TypeSiegeRepository extends JpaRepository<TypeSiege, Integer> {
    
    Optional<TypeSiege> findByNom(String nom);
    
    List<TypeSiege> findByNomContainingIgnoreCase(String nom);

    
}
