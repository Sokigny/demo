package com.cinema.demo.repository;

import com.cinema.demo.model.Paiement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaiementRepository extends JpaRepository<Paiement, Integer> {
    List<Paiement> findBySocieteId(Integer societeId);
}
