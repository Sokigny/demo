package com.cinema.demo.service;

import com.cinema.demo.model.Cinema;
import com.cinema.demo.model.Salle;
import com.cinema.demo.model.Siege;
import com.cinema.demo.model.TypeSiege;
import com.cinema.demo.repository.CinemaRepository;
import com.cinema.demo.repository.SalleRepository;
import com.cinema.demo.repository.SiegeRepository;
import com.cinema.demo.repository.TypeSiegeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SalleService {
    
    @Autowired
    private SalleRepository salleRepository;
    
    @Autowired
    private CinemaRepository cinemaRepository;
    
    @Autowired
    private SiegeRepository siegeRepository;
    
    @Autowired
    private TypeSiegeRepository typeSiegeRepository;
    
    @Autowired
    private SeanceService seanceService;
    
    // Create
    public Salle createSalle(Salle salle) {
        if (salle.getCinema() == null || salle.getCinema().getId() == null) {
            throw new RuntimeException("Le cinéma est requis");
        }
        
        Cinema cinema = cinemaRepository.findById(salle.getCinema().getId())
                .orElseThrow(() -> new RuntimeException("Cinéma non trouvé avec l'id: " + salle.getCinema().getId()));
        
        salle.setCinema(cinema);
        return salleRepository.save(salle);
    }
    
    // Read - toutes les salles
    public List<Salle> getAllSalles() {
        return salleRepository.findAll();
    }
    
    // Read - par ID
    public Optional<Salle> getSalleById(Integer id) {
        return salleRepository.findById(id);
    }
    
    // Read - par cinéma
    public List<Salle> getSallesByCinemaId(Integer cinemaId) {
        return salleRepository.findByCinemaId(cinemaId);
    }
    
    // Read - recherche par nom
    public List<Salle> searchByNom(String nom) {
        return salleRepository.findByNomContainingIgnoreCase(nom);
    }
    
    // Update
    public Salle updateSalle(Integer id, Salle salleDetails) {
        Salle salle = salleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Salle non trouvée avec l'id: " + id));
        
        if (salleDetails.getCinema() != null && salleDetails.getCinema().getId() != null) {
            Cinema cinema = cinemaRepository.findById(salleDetails.getCinema().getId())
                    .orElseThrow(() -> new RuntimeException("Cinéma non trouvé avec l'id: " + salleDetails.getCinema().getId()));
            salle.setCinema(cinema);
        }
        
        salle.setNom(salleDetails.getNom());
        
        return salleRepository.save(salle);
    }
    

    // Save (create or update)
    public Salle saveSalle(Salle salle) {
        if (salle.getCinema() != null && salle.getCinema().getId() != null) {
            Cinema cinema = cinemaRepository.findById(salle.getCinema().getId())
                    .orElseThrow(() -> new RuntimeException("Cinéma non trouvé avec l'id: " + salle.getCinema().getId()));
            salle.setCinema(cinema);
        }
        return salleRepository.save(salle);
    }
    
    // Get sieges by salle ID
    public List<Siege> getSiegesBySalle(Integer salleId) {
        return siegeRepository.findBySalleId(salleId);
    }
    
    // Générer les sièges pour une salle
    public void genererSieges(Salle salle, Integer nbRangees, Integer siegesParRangee) {
        // Récupérer le type de siège par défaut (Standard)
        List<TypeSiege> types = typeSiegeRepository.findAll();
        TypeSiege typeStandard = null;
        
        // Chercher le type "Standard" ou prendre le premier
        for (TypeSiege type : types) {
            if ("Standard".equalsIgnoreCase(type.getNom())) {
                typeStandard = type;
                break;
            }
        }
        
        if (typeStandard == null && !types.isEmpty()) {
            typeStandard = types.get(0);
        }
        
        if (typeStandard == null) {
            // Créer un type par défaut si aucun n'existe
            typeStandard = new TypeSiege("Standard");
            typeSiegeRepository.save(typeStandard);
        }

        char rangee = 'A';
        for (int r = 0; r < nbRangees; r++) {
            for (int s = 1; s <= siegesParRangee; s++) {
                Siege siege = new Siege();
                siege.setSalle(salle);
                siege.setRangee(String.valueOf(rangee));
                siege.setNumero(s);
                siege.setTypeSiege(typeStandard);
                siege.setEtat(true); // Siège disponible
                siegeRepository.save(siege);
            }
            rangee++;
        }
        
        // Mettre à jour la capacité de la salle
        salle.setCapacite(nbRangees * siegesParRangee);
        salleRepository.save(salle);
    }
    
    // Générer les sièges avec types personnalisés
    public void genererSiegesAvecTypes(Salle salle, List<Integer> typeSiegeIds, List<Integer> nombreSieges) {
        if (typeSiegeIds.size() != nombreSieges.size()) {
            throw new RuntimeException("Le nombre de types et de quantités ne correspond pas");
        }
        
        // Calculer le nombre de rangées et sièges par rangée
        int capacite = salle.getCapacite();
        int siegesParRangee = (int) Math.ceil(Math.sqrt(capacite));
        int nbRangees = (int) Math.ceil((double) capacite / siegesParRangee);
        
        int siegeIndex = 0;
        int typeIndex = 0;
        int restantPourType = nombreSieges.get(0);
        TypeSiege currentType = typeSiegeRepository.findById(typeSiegeIds.get(0))
            .orElseThrow(() -> new RuntimeException("Type de siège non trouvé"));
        
        char rangee = 'A';
        for (int r = 0; r < nbRangees; r++) {
            for (int s = 1; s <= siegesParRangee && siegeIndex < capacite; s++) {
                // Si on a utilisé tous les sièges du type actuel, passer au suivant
                if (restantPourType == 0 && typeIndex < typeSiegeIds.size() - 1) {
                    typeIndex++;
                    restantPourType = nombreSieges.get(typeIndex);
                    currentType = typeSiegeRepository.findById(typeSiegeIds.get(typeIndex))
                        .orElseThrow(() -> new RuntimeException("Type de siège non trouvé"));
                }
                
                Siege siege = new Siege();
                siege.setSalle(salle);
                siege.setRangee(String.valueOf(rangee));
                siege.setNumero(s);
                siege.setTypeSiege(currentType);
                siege.setEtat(true); // Siège disponible
                siegeRepository.save(siege);
                
                siegeIndex++;
                restantPourType--;
            }
            rangee++;
        }
    }
}
