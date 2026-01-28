package com.cinema.demo.service;

import com.cinema.demo.model.Paiement;
import com.cinema.demo.model.Publicite;
import com.cinema.demo.repository.PubliciteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class PubliciteService {
    
    @Autowired
    private PubliciteRepository publiciteRepository;
    
    @Autowired
    private PaiementService paiementService;
    
    @Autowired
    private PaiementDetailService paiementDetailService;
    
    public List<Publicite> findAll() {
        return publiciteRepository.findAll();
    }
    
    public Optional<Publicite> findById(Integer id) {
        return publiciteRepository.findById(id);
    }
    
    public Publicite save(Publicite publicite) {
        return publiciteRepository.save(publicite);
    }
    
    public void deleteById(Integer id) {
        publiciteRepository.deleteById(id);
    }
    
    public Map<String, Object> calculateCA(Integer societeId, Integer seanceId, LocalDate dateDebut, LocalDate dateFin) {
        List<Publicite> publicites = publiciteRepository.findAll();
        
        // Filtrer les publicités selon les critères
        List<Publicite> publicitesFiltrees = publicites.stream()
            .filter(p -> societeId == null || (p.getSociete() != null && p.getSociete().getId().equals(societeId)))
            .filter(p -> seanceId == null || (p.getSeance() != null && p.getSeance().getId().equals(seanceId)))
            .filter(p -> dateDebut == null || (p.getDate() != null && !p.getDate().isBefore(dateDebut)))
            .filter(p -> dateFin == null || (p.getDate() != null && !p.getDate().isAfter(dateFin)))
            .collect(Collectors.toList());
        
        // Calculer le CA total
        double caTotal = publicitesFiltrees.stream()
            .mapToDouble(p -> {
                if (p.getSociete() == null || p.getLongueur() == null || p.getNombre() == null) {
                    return 0.0;
                }
                return p.getSociete().getMontantParMin() * p.getLongueur() * p.getNombre();
            })
            .sum();
        
        // CA par société
        Map<String, Double> caBySociete = publicitesFiltrees.stream()
            .filter(p -> p.getSociete() != null && p.getLongueur() != null && p.getNombre() != null)
            .collect(Collectors.groupingBy(
                p -> p.getSociete().getNom(),
                Collectors.summingDouble(p -> p.getSociete().getMontantParMin() * p.getLongueur() * p.getNombre())
            ));
        
        // CA par mois
        Map<String, Double> caByMois = publicitesFiltrees.stream()
            .filter(p -> p.getDate() != null && p.getSociete() != null && p.getLongueur() != null && p.getNombre() != null)
            .collect(Collectors.groupingBy(
                p -> p.getDate().getYear() + "-" + String.format("%02d", p.getDate().getMonthValue()),
                Collectors.summingDouble(p -> p.getSociete().getMontantParMin() * p.getLongueur() * p.getNombre())
            ));
        
        // Détails par publicité
        List<Map<String, Object>> details = publicitesFiltrees.stream()
            .map(p -> {
                Map<String, Object> detail = new HashMap<>();
                detail.put("id", p.getId());
                detail.put("societe", p.getSociete() != null ? p.getSociete().getNom() : "N/A");
                detail.put("seance", p.getSeance() != null ? 
                    p.getSeance().getFilm().getTitre() + " - " + p.getSeance().getHeureDebut() : "N/A");
                detail.put("longueur", p.getLongueur());
                detail.put("nombre", p.getNombre());
                detail.put("date", p.getDate());
                double ca = 0.0;
                if (p.getSociete() != null && p.getLongueur() != null && p.getNombre() != null) {
                    ca = p.getSociete().getMontantParMin() * p.getLongueur() * p.getNombre();
                }
                detail.put("ca", ca);
                
                // Calculer le montant payé pour cette publicité (via sa séance)
                double montantPaye = 0.0;
                if (p.getSeance() != null && p.getSociete() != null) {
                    montantPaye = paiementDetailService.getMontantPayeParSeanceEtSociete(
                        p.getSeance().getId(), 
                        p.getSociete().getId()
                    );
                }
                detail.put("montantPaye", montantPaye);
                detail.put("resteAPayer", ca - montantPaye);
                
                return detail;
            })
            .collect(Collectors.toList());
        
        // Calculer les paiements par société
        Map<String, Double> paiementsBySociete = new HashMap<>();
        Map<String, Double> resteAPayer = new HashMap<>();
        double totalPaiements = 0.0;
        double totalResteAPayer = 0.0;
        
        for (Map.Entry<String, Double> entry : caBySociete.entrySet()) {
            String societeName = entry.getKey();
            Double ca = entry.getValue();
            
            // Récupérer les paiements pour cette société (basé sur la période si spécifiée)
            List<Paiement> paiements = paiementService.getAllPaiements().stream()
                .filter(p -> p.getSociete() != null && p.getSociete().getNom().equals(societeName))
                .filter(p -> {
                    if (dateDebut != null && dateFin != null) {
                        return !p.getDatePaiement().isBefore(dateDebut) && 
                               !p.getDatePaiement().isAfter(dateFin);
                    }
                    return true;
                })
                .collect(Collectors.toList());
            
            double montantPaye = paiements.stream()
                .mapToDouble(p -> p.getMontant() != null ? p.getMontant() : 0)
                .sum();
            
            double reste = ca - montantPaye;
            
            paiementsBySociete.put(societeName, montantPaye);
            resteAPayer.put(societeName, reste);
            totalPaiements += montantPaye;
            totalResteAPayer += reste;
        }
        
        // Construire le résultat
        Map<String, Object> result = new HashMap<>();
        result.put("caTotal", caTotal);
        result.put("caBySociete", caBySociete);
        result.put("paiementsBySociete", paiementsBySociete);
        result.put("resteAPayerBySociete", resteAPayer);
        result.put("totalPaiements", totalPaiements);
        result.put("totalResteAPayer", totalResteAPayer);
        result.put("caByMois", caByMois);
        result.put("details", details);
        result.put("nombrePublicites", publicitesFiltrees.size());
        
        return result;
    }
}
