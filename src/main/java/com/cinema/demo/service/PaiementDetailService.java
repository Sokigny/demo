package com.cinema.demo.service;

import com.cinema.demo.model.PaiementDetail;
import com.cinema.demo.repository.PaiementDetailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PaiementDetailService {

    @Autowired
    private PaiementDetailRepository paiementDetailRepository;

    public List<PaiementDetail> getAllDetails() {
        return paiementDetailRepository.findAll();
    }

    public List<PaiementDetail> getDetailsByPaiement(Integer paiementId) {
        return paiementDetailRepository.findByPaiementId(paiementId);
    }

    public List<PaiementDetail> getDetailsBySeance(Integer seanceId) {
        return paiementDetailRepository.findBySeanceId(seanceId);
    }

    public PaiementDetail saveDetail(PaiementDetail detail) {
        return paiementDetailRepository.save(detail);
    }

    public void deleteDetail(Integer id) {
        paiementDetailRepository.deleteById(id);
    }
    
    public void deleteByPaiement(Integer paiementId) {
        List<PaiementDetail> details = paiementDetailRepository.findByPaiementId(paiementId);
        paiementDetailRepository.deleteAll(details);
    }
    
    /**
     * Calcule le montant total payé pour une séance et une société données
     */
    public double getMontantPayeParSeanceEtSociete(Integer seanceId, Integer societeId) {
        List<PaiementDetail> details = paiementDetailRepository.findBySeanceId(seanceId);
        
        return details.stream()
            .filter(d -> d.getPaiement() != null && 
                        d.getPaiement().getSociete() != null && 
                        d.getPaiement().getSociete().getId().equals(societeId))
            .mapToDouble(d -> d.getMontant() != null ? d.getMontant() : 0.0)
            .sum();
    }
}
