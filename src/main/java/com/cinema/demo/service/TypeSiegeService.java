package com.cinema.demo.service;

import com.cinema.demo.model.TypeSiege;
import com.cinema.demo.repository.TypeSiegeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TypeSiegeService {
    
    @Autowired
    private TypeSiegeRepository typeSiegeRepository;
    
    // Create
    public TypeSiege createTypeSiege(TypeSiege typeSiege) {
        return typeSiegeRepository.save(typeSiege);
    }
    
    // Read - tous les types de siège
    public List<TypeSiege> getAllTypesSiege() {
        return typeSiegeRepository.findAll();
    }
    
    // Alias pour getAllTypesSiege
    public List<TypeSiege> getAllTypeSieges() {
        return getAllTypesSiege();
    }
    
    // Read - par ID
    public Optional<TypeSiege> getTypeSiegeById(Integer id) {
        return typeSiegeRepository.findById(id);
    }
    
    // Read - par nom
    public Optional<TypeSiege> getTypeSiegeByNom(String nom) {
        return typeSiegeRepository.findByNom(nom);
    }
    
    // Read - recherche par nom
    public List<TypeSiege> searchByNom(String nom) {
        return typeSiegeRepository.findByNomContainingIgnoreCase(nom);
    }
    
    // Update
    public TypeSiege updateTypeSiege(Integer id, TypeSiege typeSiegeDetails) {
        TypeSiege typeSiege = typeSiegeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Type de siège non trouvé avec l'id: " + id));
        
        typeSiege.setNom(typeSiegeDetails.getNom());
        
        return typeSiegeRepository.save(typeSiege);
    }
    
    // Delete
    public void deleteTypeSiege(Integer id) {
        TypeSiege typeSiege = typeSiegeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Type de siège non trouvé avec l'id: " + id));
        typeSiegeRepository.delete(typeSiege);
    }
}
