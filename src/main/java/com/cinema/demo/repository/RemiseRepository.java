package com.cinema.demo.repository;

import com.cinema.demo.model.Remise;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RemiseRepository extends JpaRepository<Remise, Integer> {
    
    // Trouver toutes les remises pour un type de siège
    List<Remise> findByTypeSiegeId(Integer typeSiegeId);
    
    // Trouver les remises applicables pour un âge donné
    List<Remise> findByAgeMinLessThanEqualAndAgeMaxGreaterThanEqual(Integer age1, Integer age2);
    
    // Trouver les remises pour un type de siège et un âge donné
    List<Remise> findByTypeSiegeIdAndAgeMinLessThanEqualAndAgeMaxGreaterThanEqual(
        Integer typeSiegeId, Integer age1, Integer age2);
    
    // Trouver une remise spécifique par type de siège et tranche d'âge
    Optional<Remise> findByTypeSiegeIdAndAgeMinAndAgeMax(
        Integer typeSiegeId, Integer ageMin, Integer ageMax);
    
    // Vérifier si une remise existe pour un type de siège et une tranche d'âge
    boolean existsByTypeSiegeIdAndAgeMinAndAgeMax(
        Integer typeSiegeId, Integer ageMin, Integer ageMax);
}
