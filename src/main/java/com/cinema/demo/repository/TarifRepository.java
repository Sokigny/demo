package com.cinema.demo.repository;

import com.cinema.demo.model.Tarif;
import com.cinema.demo.model.TypePersonne;
import com.cinema.demo.model.TypeSiege;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TarifRepository extends JpaRepository<Tarif, Integer> {
    
    List<Tarif> findByTypePersonne(TypePersonne typePersonne);
    
    List<Tarif> findByTypePersonneId(Integer typePersonneId);
    
    List<Tarif> findByTypeSiege(TypeSiege typeSiege);
    
    List<Tarif> findByTypeSiegeId(Integer typeSiegeId);
    
    List<Tarif> findByTypePersonneIdAndTypeSiegeId(Integer typePersonneId, Integer typeSiegeId);
    
    List<Tarif> findByMontantGreaterThanEqual(Integer montant);
    
    List<Tarif> findByPourcentageGreaterThanEqual(Double pourcentage);
}
