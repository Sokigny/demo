package com.cinema.demo.service;

import com.cinema.demo.model.Cinema;
import com.cinema.demo.repository.CinemaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CinemaService {
    
    @Autowired
    private CinemaRepository cinemaRepository;
    
    // Create
    public Cinema createCinema(Cinema cinema) {
        return cinemaRepository.save(cinema);
    }
    
    // Read - tous les cinémas
    public List<Cinema> getAllCinemas() {
        return cinemaRepository.findAll();
    }
    
    // Read - par ID
    public Optional<Cinema> getCinemaById(Integer id) {
        return cinemaRepository.findById(id);
    }
    
    // Read - recherche par nom
    public List<Cinema> searchByNom(String nom) {
        return cinemaRepository.findByNomContainingIgnoreCase(nom);
    }
    
    // Read - recherche par adresse
    public List<Cinema> searchByAdresse(String adresse) {
        return cinemaRepository.findByAdresseContainingIgnoreCase(adresse);
    }
    
    // Update
    public Cinema updateCinema(Integer id, Cinema cinemaDetails) {
        Cinema cinema = cinemaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cinéma non trouvé avec l'id: " + id));
        
        cinema.setNom(cinemaDetails.getNom());
        cinema.setAdresse(cinemaDetails.getAdresse());
        
        return cinemaRepository.save(cinema);
    }
    
    // Delete
    public void deleteCinema(Integer id) {
        Cinema cinema = cinemaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cinéma non trouvé avec l'id: " + id));
        cinemaRepository.delete(cinema);
    }
}
