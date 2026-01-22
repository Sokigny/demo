package com.cinema.demo.repository;

import com.cinema.demo.model.Film;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FilmRepository extends JpaRepository<Film, Integer> {
    
    List<Film> findByTitreContainingIgnoreCase(String titre);
}
