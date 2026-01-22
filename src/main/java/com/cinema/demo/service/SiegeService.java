package com.cinema.demo.service;

import com.cinema.demo.model.Salle;
import com.cinema.demo.model.Siege;
import com.cinema.demo.model.TypeSiege;
import com.cinema.demo.repository.SalleRepository;
import com.cinema.demo.repository.SiegeRepository;
import com.cinema.demo.repository.TypeSiegeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SiegeService {
    
    @Autowired
    private SiegeRepository siegeRepository;
    
    @Autowired
    private SalleRepository salleRepository;
    
    @Autowired
    private TypeSiegeRepository typeSiegeRepository;
    
    // Create
    public Siege createSiege(Siege siege) {
        if (siege.getSalle() == null || siege.getSalle().getId() == null) {
            throw new RuntimeException("La salle est requise");
        }
        
        if (siege.getTypeSiege() == null || siege.getTypeSiege().getId() == null) {
            throw new RuntimeException("Le type de siège est requis");
        }
        
        Salle salle = salleRepository.findById(siege.getSalle().getId())
                .orElseThrow(() -> new RuntimeException("Salle non trouvée avec l'id: " + siege.getSalle().getId()));
        
        TypeSiege typeSiege = typeSiegeRepository.findById(siege.getTypeSiege().getId())
                .orElseThrow(() -> new RuntimeException("Type de siège non trouvé avec l'id: " + siege.getTypeSiege().getId()));
        
        siege.setSalle(salle);
        siege.setTypeSiege(typeSiege);
        
        return siegeRepository.save(siege);
    }
    
    // Read - tous les sièges
    public List<Siege> getAllSieges() {
        return siegeRepository.findAll();
    }
    
    // Read - par ID
    public Optional<Siege> getSiegeById(Integer id) {
        return siegeRepository.findById(id);
    }
    
    // Read - par salle
    public List<Siege> getSiegesBySalleId(Integer salleId) {
        return siegeRepository.findBySalleId(salleId);
    }
    
    // Read - par type de siège
    public List<Siege> getSiegesByTypeSiegeId(Integer typeSiegeId) {
        return siegeRepository.findByTypeSiegeId(typeSiegeId);
    }
    
    // Read - par état
    public List<Siege> getSiegesByEtat(Boolean etat) {
        return siegeRepository.findByEtat(etat);
    }
    
    // Read - par salle et état
    public List<Siege> getSiegesBySalleIdAndEtat(Integer salleId, Boolean etat) {
        return siegeRepository.findBySalleIdAndEtat(salleId, etat);
    }
    
    // Read - par salle et rangée
    public List<Siege> getSiegesBySalleIdAndRangee(Integer salleId, String rangee) {
        return siegeRepository.findBySalleIdAndRangee(salleId, rangee);
    }
    
    // Update
    public Siege updateSiege(Integer id, Siege siegeDetails) {
        Siege siege = siegeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Siège non trouvé avec l'id: " + id));
        
        if (siegeDetails.getSalle() != null && siegeDetails.getSalle().getId() != null) {
            Salle salle = salleRepository.findById(siegeDetails.getSalle().getId())
                    .orElseThrow(() -> new RuntimeException("Salle non trouvée avec l'id: " + siegeDetails.getSalle().getId()));
            siege.setSalle(salle);
        }
        
        if (siegeDetails.getTypeSiege() != null && siegeDetails.getTypeSiege().getId() != null) {
            TypeSiege typeSiege = typeSiegeRepository.findById(siegeDetails.getTypeSiege().getId())
                    .orElseThrow(() -> new RuntimeException("Type de siège non trouvé avec l'id: " + siegeDetails.getTypeSiege().getId()));
            siege.setTypeSiege(typeSiege);
        }
        
        siege.setRangee(siegeDetails.getRangee());
        siege.setNumero(siegeDetails.getNumero());
        siege.setEtat(siegeDetails.getEtat());
        
        return siegeRepository.save(siege);
    }
    
    // Delete
    public void deleteSiege(Integer id) {
        Siege siege = siegeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Siège non trouvé avec l'id: " + id));
        siegeRepository.delete(siege);
    }
}
