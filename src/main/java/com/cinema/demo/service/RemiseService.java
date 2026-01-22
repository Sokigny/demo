package com.cinema.demo.service;

import com.cinema.demo.model.Remise;
import com.cinema.demo.model.TypeSiege;
import com.cinema.demo.repository.RemiseRepository;
import com.cinema.demo.repository.TypeSiegeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RemiseService {
    
    @Autowired
    private RemiseRepository remiseRepository;
    
    @Autowired
    private TypeSiegeRepository typeSiegeRepository;
    
    // Create
    public Remise createRemise(Remise remise) {
        // Validation: vérifier que le type de siège existe
        if (remise.getTypeSiege() == null || remise.getTypeSiege().getId() == null) {
            throw new RuntimeException("Le type de siège est requis");
        }
        
        TypeSiege typeSiege = typeSiegeRepository.findById(remise.getTypeSiege().getId())
                .orElseThrow(() -> new RuntimeException("Type de siège non trouvé avec l'id: " + remise.getTypeSiege().getId()));
        
        // Validation: vérifier que ageMin <= ageMax
        if (remise.getAgeMin() != null && remise.getAgeMax() != null && remise.getAgeMin() > remise.getAgeMax()) {
            throw new RuntimeException("L'âge minimum ne peut pas être supérieur à l'âge maximum");
        }
        
        // Validation: vérifier qu'une remise similaire n'existe pas déjà
        if (remiseRepository.existsByTypeSiegeIdAndAgeMinAndAgeMax(
                typeSiege.getId(), remise.getAgeMin(), remise.getAgeMax())) {
            throw new RuntimeException("Une remise existe déjà pour ce type de siège et cette tranche d'âge");
        }
        
        remise.setTypeSiege(typeSiege);
        return remiseRepository.save(remise);
    }
    
    // Read - toutes les remises
    public List<Remise> getAllRemises() {
        return remiseRepository.findAll();
    }
    
    // Read - par ID
    public Optional<Remise> getRemiseById(Integer id) {
        return remiseRepository.findById(id);
    }
    
    // Read - par type de siège
    public List<Remise> getRemisesByTypeSiegeId(Integer typeSiegeId) {
        return remiseRepository.findByTypeSiegeId(typeSiegeId);
    }
    
    // Read - remises applicables pour un âge donné
    public List<Remise> getRemisesForAge(Integer age) {
        if (age == null) {
            throw new RuntimeException("L'âge est requis");
        }
        return remiseRepository.findByAgeMinLessThanEqualAndAgeMaxGreaterThanEqual(age, age);
    }
    
    // Read - remises applicables pour un type de siège et un âge donné
    public List<Remise> getRemisesForTypeSiegeAndAge(Integer typeSiegeId, Integer age) {
        if (typeSiegeId == null) {
            throw new RuntimeException("Le type de siège est requis");
        }
        if (age == null) {
            throw new RuntimeException("L'âge est requis");
        }
        return remiseRepository.findByTypeSiegeIdAndAgeMinLessThanEqualAndAgeMaxGreaterThanEqual(
                typeSiegeId, age, age);
    }
    
    // Méthode utilitaire: obtenir la meilleure remise (la plus avantageuse)
    public Optional<Remise> getMeilleureRemiseForTypeSiegeAndAge(Integer typeSiegeId, Integer age) {
        List<Remise> remises = getRemisesForTypeSiegeAndAge(typeSiegeId, age);
        return remises.stream()
                .max((r1, r2) -> Integer.compare(r1.getMontant(), r2.getMontant()));
    }
    
    // Méthode utilitaire: calculer le prix après remise
    public Integer calculerPrixAvecRemise(Integer typeSiegeId, Integer age, Integer prixOriginal) {
        if (prixOriginal == null || typeSiegeId == null || age == null) {
            return prixOriginal;
        }
        
        Optional<Remise> meilleureRemise = getMeilleureRemiseForTypeSiegeAndAge(typeSiegeId, age);
        
        if (meilleureRemise.isPresent()) {
            return meilleureRemise.get().calculerPrixApresRemise(prixOriginal);
        }
        
        return prixOriginal;
    }
    
    // Update
    public Remise updateRemise(Integer id, Remise remiseDetails) {
        Remise remise = remiseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Remise non trouvée avec l'id: " + id));
        
        // Validation: vérifier que ageMin <= ageMax
        Integer newAgeMin = remiseDetails.getAgeMin() != null ? remiseDetails.getAgeMin() : remise.getAgeMin();
        Integer newAgeMax = remiseDetails.getAgeMax() != null ? remiseDetails.getAgeMax() : remise.getAgeMax();
        
        if (newAgeMin > newAgeMax) {
            throw new RuntimeException("L'âge minimum ne peut pas être supérieur à l'âge maximum");
        }
        
        // Mettre à jour le type de siège si fourni
        if (remiseDetails.getTypeSiege() != null && remiseDetails.getTypeSiege().getId() != null) {
            TypeSiege typeSiege = typeSiegeRepository.findById(remiseDetails.getTypeSiege().getId())
                    .orElseThrow(() -> new RuntimeException("Type de siège non trouvé avec l'id: " + remiseDetails.getTypeSiege().getId()));
            remise.setTypeSiege(typeSiege);
        }
        
        // Mettre à jour les autres champs
        if (remiseDetails.getMontant() != null) {
            remise.setMontant(remiseDetails.getMontant());
        }
        if (remiseDetails.getAgeMin() != null) {
            remise.setAgeMin(remiseDetails.getAgeMin());
        }
        if (remiseDetails.getAgeMax() != null) {
            remise.setAgeMax(remiseDetails.getAgeMax());
        }
        
        return remiseRepository.save(remise);
    }
    
    // Delete
    public void deleteRemise(Integer id) {
        Remise remise = remiseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Remise non trouvée avec l'id: " + id));
        remiseRepository.delete(remise);
    }
    
    // Delete - toutes les remises d'un type de siège
    public void deleteRemisesByTypeSiege(Integer typeSiegeId) {
        List<Remise> remises = remiseRepository.findByTypeSiegeId(typeSiegeId);
        remiseRepository.deleteAll(remises);
    }
}
