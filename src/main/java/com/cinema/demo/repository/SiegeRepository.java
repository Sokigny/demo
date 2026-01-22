package com.cinema.demo.repository;

import com.cinema.demo.model.Siege;
import com.cinema.demo.model.Salle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SiegeRepository extends JpaRepository<Siege, Integer> {
    
    List<Siege> findBySalleId(Integer salleId);
    
    List<Siege> findBySalle(Salle salle);
    
    List<Siege> findByTypeSiegeId(Integer typeSiegeId);
    
    List<Siege> findByEtat(Boolean etat);
    
    List<Siege> findBySalleIdAndEtat(Integer salleId, Boolean etat);
    
    List<Siege> findBySalleIdAndRangee(Integer salleId, String rangee);
    
    // Compter les sièges par type pour une salle donnée
    List<Siege> findBySalleIdAndTypeSiegeId(Integer salleId, Integer typeSiegeId);
}
