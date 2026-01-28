package com.cinema.demo.service;

import com.cinema.demo.model.Tarif;
import com.cinema.demo.model.TypePersonne;
import com.cinema.demo.model.TypeSiege;
import com.cinema.demo.repository.TarifRepository;
import com.cinema.demo.repository.TypePersonneRepository;
import com.cinema.demo.repository.TypeSiegeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TarifService {
    
    @Autowired
    private TarifRepository tarifRepository;
    
    @Autowired
    private TypePersonneRepository typePersonneRepository;
    
    @Autowired
    private TypeSiegeRepository typeSiegeRepository;
    
    // Create
    public Tarif createTarif(Tarif tarif) {
        return tarifRepository.save(tarif);
    }
    
    // Create avec typePersonneId et typeSiegeId
    public Tarif createTarif(Integer typePersonneId, Integer typeSiegeId, Integer montant, Double pourcentage) {
        TypePersonne typePersonne = typePersonneRepository.findById(typePersonneId)
                .orElseThrow(() -> new RuntimeException("Type de personne non trouvé avec l'id: " + typePersonneId));
        
        TypeSiege typeSiege = typeSiegeRepository.findById(typeSiegeId)
                .orElseThrow(() -> new RuntimeException("Type de siège non trouvé avec l'id: " + typeSiegeId));
        
        Tarif tarif = new Tarif(typePersonne, typeSiege, montant, pourcentage);
        return tarifRepository.save(tarif);
    }
    
    // Read - tous les tarifs
    public List<Tarif> getAllTarifs() {
        return tarifRepository.findAll();
    }
    
    // Read - par ID
    public Optional<Tarif> getTarifById(Integer id) {
        return tarifRepository.findById(id);
    }
    
    // Read - par type de personne
    public List<Tarif> getTarifsByTypePersonne(TypePersonne typePersonne) {
        return tarifRepository.findByTypePersonne(typePersonne);
    }
    
    // Read - par type de personne ID
    public List<Tarif> getTarifsByTypePersonneId(Integer typePersonneId) {
        return tarifRepository.findByTypePersonneId(typePersonneId);
    }
    
    // Read - par type de siège
    public List<Tarif> getTarifsByTypeSiege(TypeSiege typeSiege) {
        return tarifRepository.findByTypeSiege(typeSiege);
    }
    
    // Read - par type de siège ID
    public List<Tarif> getTarifsByTypeSiegeId(Integer typeSiegeId) {
        return tarifRepository.findByTypeSiegeId(typeSiegeId);
    }
    
    // Read - par type de personne ID et type de siège ID
    public List<Tarif> getTarifsByTypePersonneIdAndTypeSiegeId(Integer typePersonneId, Integer typeSiegeId) {
        return tarifRepository.findByTypePersonneIdAndTypeSiegeId(typePersonneId, typeSiegeId);
    }
    
    // Read - tarif spécifique pour un type de personne et un type de siège
    public Optional<Tarif> getTarif(Integer typePersonneId, Integer typeSiegeId) {
        List<Tarif> tarifs = tarifRepository.findByTypePersonneIdAndTypeSiegeId(typePersonneId, typeSiegeId);
        return tarifs.isEmpty() ? Optional.empty() : Optional.of(tarifs.get(0));
    }
    
    // Read - tarif avec objets TypePersonne et TypeSiege
    public Optional<Tarif> getTarif(TypePersonne typePersonne, TypeSiege typeSiege) {
        if (typePersonne == null || typePersonne.getId() == null || typeSiege == null || typeSiege.getId() == null) {
            return Optional.empty();
        }
        return getTarif(typePersonne.getId(), typeSiege.getId());
    }
    
    // Read - par montant minimum
    public List<Tarif> getTarifsByMontantMinimum(Integer montant) {
        return tarifRepository.findByMontantGreaterThanEqual(montant);
    }
    
    // Read - par pourcentage minimum
    public List<Tarif> getTarifsByPourcentageMinimum(Double pourcentage) {
        return tarifRepository.findByPourcentageGreaterThanEqual(pourcentage);
    }
    
    // Update
    public Tarif updateTarif(Integer id, Tarif tarifDetails) {
        Tarif tarif = tarifRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tarif non trouvé avec l'id: " + id));
        
        if (tarifDetails.getTypePersonne() != null) {
            tarif.setTypePersonne(tarifDetails.getTypePersonne());
        }
        if (tarifDetails.getTypeSiege() != null) {
            tarif.setTypeSiege(tarifDetails.getTypeSiege());
        }
        if (tarifDetails.getMontant() != null) {
            tarif.setMontant(tarifDetails.getMontant());
        }
        if (tarifDetails.getPourcentage() != null) {
            tarif.setPourcentage(tarifDetails.getPourcentage());
        }
        
        return tarifRepository.save(tarif);
    }
    
    // Update avec typePersonneId et typeSiegeId
    public Tarif updateTarif(Integer id, Integer typePersonneId, Integer typeSiegeId, Integer montant, Double pourcentage) {
        Tarif tarif = tarifRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tarif non trouvé avec l'id: " + id));
        
        if (typePersonneId != null) {
            TypePersonne typePersonne = typePersonneRepository.findById(typePersonneId)
                    .orElseThrow(() -> new RuntimeException("Type de personne non trouvé avec l'id: " + typePersonneId));
            tarif.setTypePersonne(typePersonne);
        }
        if (typeSiegeId != null) {
            TypeSiege typeSiege = typeSiegeRepository.findById(typeSiegeId)
                    .orElseThrow(() -> new RuntimeException("Type de siège non trouvé avec l'id: " + typeSiegeId));
            tarif.setTypeSiege(typeSiege);
        }
        if (montant != null) {
            tarif.setMontant(montant);
        }
        if (pourcentage != null) {
            tarif.setPourcentage(pourcentage);
        }
        
        return tarifRepository.save(tarif);
    }
    
    // Delete
    public void deleteTarif(Integer id) {
        Tarif tarif = tarifRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tarif non trouvé avec l'id: " + id));
        tarifRepository.delete(tarif);
    }
}
