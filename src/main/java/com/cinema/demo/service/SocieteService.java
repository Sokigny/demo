package com.cinema.demo.service;

import com.cinema.demo.model.Paiement;
import com.cinema.demo.model.Publicite;
import com.cinema.demo.model.Societe;
import com.cinema.demo.repository.SocieteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SocieteService {
    
    @Autowired
    private SocieteRepository societeRepository;
    
    @Autowired
    private PubliciteService publiciteService;
    
    @Autowired
    private PaiementService paiementService;
    
    public List<Societe> findAll() {
        return societeRepository.findAll();
    }
    
    public Optional<Societe> findById(Integer id) {
        return societeRepository.findById(id);
    }
    
    public Societe save(Societe societe) {
        return societeRepository.save(societe);
    }
    
    public void deleteById(Integer id) {
        societeRepository.deleteById(id);
    }
    
    public List<Societe> getAllSocietes() {
        return societeRepository.findAll();
    }
    
    /**
     * Calcule le reste à payer pour une société
     * @param societeId L'ID de la société
     * @return Le montant restant à payer (CA - Paiements)
     */
    public double getResteAPayer(Integer societeId) {
        // Calculer le CA total pour cette société
        List<Publicite> publicites = publiciteService.findAll().stream()
            .filter(p -> p.getSociete() != null && p.getSociete().getId().equals(societeId))
            .toList();
        
        double caTotal = publicites.stream()
            .filter(p -> p.getSociete() != null && p.getLongueur() != null && p.getNombre() != null)
            .mapToDouble(p -> p.getSociete().getMontantParMin() * p.getLongueur() * p.getNombre())
            .sum();
        
        // Calculer le total des paiements pour cette société
        List<Paiement> paiements = paiementService.getAllPaiements().stream()
            .filter(p -> p.getSociete() != null && p.getSociete().getId().equals(societeId))
            .toList();
        
        double totalPaiements = paiements.stream()
            .mapToDouble(p -> p.getMontant() != null ? p.getMontant() : 0)
            .sum();
        
        return caTotal - totalPaiements;
    }
}
