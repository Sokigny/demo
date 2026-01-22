package com.cinema.demo.service;

import com.cinema.demo.model.Film;
import com.cinema.demo.model.Seance;
import com.cinema.demo.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class StatisticsService {

    @Autowired
    private FilmRepository filmRepository;

    @Autowired
    private SalleRepository salleRepository;

    @Autowired
    private SeanceRepository seanceRepository;

    @Autowired
    private ReservationRepository reservationRepository;

    public long getTotalFilms() {
        return filmRepository.count();
    }

    public long getTotalSalles() {
        return salleRepository.count();
    }

    public long getTotalSeances() {
        return seanceRepository.count();
    }

    public long getTotalReservations() {
        return reservationRepository.count();
    }

    public List<Seance> getProchainesSeances(int limit) {
        LocalDate today = LocalDate.now();
        return seanceRepository.findByDateSeanceGreaterThanEqualOrderByDateSeanceAscHeureDebutAsc(today)
                .stream()
                .limit(limit)
                .toList();
    }

    public List<Film> getFilmsRecents(int limit) {
        return filmRepository.findAll()
                .stream()
                .limit(limit)
                .toList();
    }
}
