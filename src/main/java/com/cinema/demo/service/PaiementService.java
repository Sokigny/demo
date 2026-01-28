package com.cinema.demo.service;

import com.cinema.demo.model.Paiement;
import com.cinema.demo.model.PaiementDetail;
import com.cinema.demo.model.Seance;
import com.cinema.demo.repository.PaiementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class PaiementService {
    
    @Autowired
    private PaiementRepository paiementRepository;
    
    @Autowired
    private PaiementDetailService paiementDetailService;
    
    @Autowired
    private SeanceService seanceService;
    
    public List<Paiement> getAllPaiements() {
        return paiementRepository.findAll();
    }
    
    public Optional<Paiement> getPaiementById(Integer id) {
        return paiementRepository.findById(id);
    }
    
    public List<Paiement> getPaiementsBySociete(Integer societeId) {
        return paiementRepository.findBySocieteId(societeId);
    }
    
    public Paiement savePaiement(Paiement paiement) {
        return paiementRepository.save(paiement);
    }
    
    @Transactional
    public void deletePaiement(Integer id) {
        paiementDetailService.deleteByPaiement(id);
        paiementRepository.deleteById(id);
    }
    
    /**
     * Crée un paiement et répartit automatiquement le montant sur les séances
     * selon le reste à payer de chaque séance
     */
    @Transactional
    public Map<String, Object> createPaiementAvecRepartition(Paiement paiement) {
        Integer societeId = paiement.getSociete().getId();
        double montantPaiement = paiement.getMontant();
        
        // Calculer le reste à payer par séance pour cette société
        Map<String, Object> caData = seanceService.calculateCAParSeance(societeId, null, null, null);
        List<Map<String, Object>> seancesDetails = (List<Map<String, Object>>) caData.get("details");
        
        // Filtrer les séances avec reste > 0 et de la société
        Map<Integer, Double> resteParSeance = new HashMap<>();
        double resteTotal = 0.0;
        
        for (Map<String, Object> detail : seancesDetails) {
            Double resteAPayer = (Double) detail.get("resteAPayerPublicites");
            if (resteAPayer > 0) {
                Integer seanceId = (Integer) detail.get("seanceId");
                resteParSeance.put(seanceId, resteAPayer);
                resteTotal += resteAPayer;
            }
        }
        
        // Vérifier que le montant ne dépasse pas le reste total
        if (montantPaiement > resteTotal) {
            throw new IllegalArgumentException(
                String.format("Le montant du paiement (%.2f Ar) dépasse le reste à payer total (%.2f Ar) de la société.", 
                    montantPaiement, resteTotal)
            );
        }
        
        // Calculer le pourcentage
        double pourcentage = montantPaiement / resteTotal;
        
        // Sauvegarder le paiement parent
        Paiement savedPaiement = paiementRepository.save(paiement);
        
        // Créer les détails de répartition
        List<Map<String, Object>> repartitionDetails = new ArrayList<>();
        double totalReparti = 0.0;
        
        for (Map.Entry<Integer, Double> entry : resteParSeance.entrySet()) {
            Integer seanceId = entry.getKey();
            Double resteSeance = entry.getValue();
            double montantAffecte = resteSeance * pourcentage;
            
            // Créer le détail
            Seance seance = seanceService.getSeanceById(seanceId).orElseThrow();
            PaiementDetail detail = new PaiementDetail(savedPaiement, seance, montantAffecte);
            paiementDetailService.saveDetail(detail);
            
            // Pour l'affichage
            Map<String, Object> info = new HashMap<>();
            info.put("seanceId", seanceId);
            info.put("film", seance.getFilm().getTitre());
            info.put("dateSeance", seance.getDateSeance());
            info.put("heureDebut", seance.getHeureDebut());
            info.put("resteAvant", resteSeance);
            info.put("montantAffecte", montantAffecte);
            info.put("pourcentage", pourcentage * 100);
            repartitionDetails.add(info);
            
            totalReparti += montantAffecte;
        }
        
        // Résultat
        Map<String, Object> result = new HashMap<>();
        result.put("paiement", savedPaiement);
        result.put("details", repartitionDetails);
        result.put("totalReparti", totalReparti);
        result.put("pourcentage", pourcentage * 100);
        result.put("nbSeances", resteParSeance.size());
        
        return result;
    }
}
