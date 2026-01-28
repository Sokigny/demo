package com.cinema.demo.service;

import com.cinema.demo.model.TypePersonne;
import com.cinema.demo.repository.TypePersonneRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class TypePersonneService {
    
    @Autowired
    private TypePersonneRepository typePersonneRepository;
    
    // Create
    public TypePersonne createTypePersonne(TypePersonne typePersonne) {
        return typePersonneRepository.save(typePersonne);
    }
    
    // Read - tous les types de personne
    public List<TypePersonne> getAllTypesPersonne() {
        return typePersonneRepository.findAll();
    }
    
    // Read - par ID
    public Optional<TypePersonne> getTypePersonneById(Integer id) {
        return typePersonneRepository.findById(id);
    }
    
    // Read - par nom
    public Optional<TypePersonne> getTypePersonneByNom(String nom) {
        return typePersonneRepository.findByNom(nom);
    }
    
    // Read - recherche par nom
    public List<TypePersonne> searchByNom(String nom) {
        return typePersonneRepository.findByNomContainingIgnoreCase(nom);
    }
    
    // Update
    public TypePersonne updateTypePersonne(Integer id, TypePersonne typePersonneDetails) {
        TypePersonne typePersonne = typePersonneRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Type de personne non trouvé avec l'id: " + id));
        
        typePersonne.setNom(typePersonneDetails.getNom());
        if (typePersonneDetails.getAgeMin() != null) {
            typePersonne.setAgeMin(typePersonneDetails.getAgeMin());
        }
        if (typePersonneDetails.getAgeMax() != null) {
            typePersonne.setAgeMax(typePersonneDetails.getAgeMax());
        }
        
        return typePersonneRepository.save(typePersonne);
    }
    
    // Calculer l'âge à partir de la date de naissance
    private int calculerAge(Date dateNaissance) {
        LocalDate dateNaissanceLocal = new java.sql.Date(dateNaissance.getTime()).toLocalDate();
        LocalDate dateActuelle = LocalDate.now();
        return Period.between(dateNaissanceLocal, dateActuelle).getYears();
    }
    
    // Trouver le type de personne par date de naissance
    public Optional<TypePersonne> getTypeDePersonne(Date dateNaissance) {
        int age = calculerAge(dateNaissance);
        
        List<TypePersonne> tousTypesPersonne = typePersonneRepository.findAll();
        
        for (TypePersonne typePersonne : tousTypesPersonne) {
            Integer ageMin = typePersonne.getAgeMin();
            Integer ageMax = typePersonne.getAgeMax();
            
            // Vérifier si l'âge est dans la plage
            boolean ageMinOk = (ageMin == null) || (age >= ageMin);
            boolean ageMaxOk = (ageMax == null) || (age <= ageMax);
            
            if (ageMinOk && ageMaxOk) {
                return Optional.of(typePersonne);
            }
        }
        
        return Optional.empty();
    }
    
    // Delete
    public void deleteTypePersonne(Integer id) {
        TypePersonne typePersonne = typePersonneRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Type de personne non trouvé avec l'id: " + id));
        typePersonneRepository.delete(typePersonne);
    }
}
