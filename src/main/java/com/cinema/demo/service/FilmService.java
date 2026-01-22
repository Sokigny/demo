package com.cinema.demo.service;

import com.cinema.demo.model.Film;
import com.cinema.demo.repository.FilmRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FilmService {
    
    @Autowired
    private FilmRepository filmRepository;
    
    // Create
    public Film createFilm(Film film) {
        return filmRepository.save(film);
    }
    
    // Read - tous les films
    public List<Film> getAllFilms() {
        return filmRepository.findAll();
    }
    
    // Read - par ID
    public Optional<Film> getFilmById(Integer id) {
        return filmRepository.findById(id);
    }
    
    // Save (create or update)
    public Film saveFilm(Film film) {
        return filmRepository.save(film);
    }
    
    // Read - recherche par titre
    public List<Film> searchByTitre(String titre) {
        return filmRepository.findByTitreContainingIgnoreCase(titre);
    }
    
    // Update
    public Film updateFilm(Integer id, Film filmDetails) {
        Film film = filmRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Film non trouvé avec l'id: " + id));
        
        film.setTitre(filmDetails.getTitre());
        
        return filmRepository.save(film);
    }
    
    // Delete
    public void deleteFilm(Integer id) {
        Film film = filmRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Film non trouvé avec l'id: " + id));
        filmRepository.delete(film);
    }
}
