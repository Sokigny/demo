package com.cinema.demo.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.LocalDateTime;

@Entity
@Table(name = "seances")
public class Seance {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @ManyToOne
    @JoinColumn(name = "film_id", nullable = false)
    private Film film;
    
    @ManyToOne
    @JoinColumn(name = "salle_id", nullable = false)
    private Salle salle;
    
    @Column(name = "date_seance", nullable = false)
    private LocalDate dateSeance;
    
    @Column(name = "heure_debut", nullable = false)
    private LocalTime heureDebut;
    
    @Column(name = "heure_fin", nullable = false)
    private LocalTime heureFin;
    
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    // Constructeurs
    public Seance() {
    }

    public Seance(Film film, Salle salle, LocalDate dateSeance, LocalTime heureDebut, LocalTime heureFin) {
        this.film = film;
        this.salle = salle;
        this.dateSeance = dateSeance;
        this.heureDebut = heureDebut;
        this.heureFin = heureFin;
    }

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    // Getters et Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Film getFilm() {
        return film;
    }

    public void setFilm(Film film) {
        this.film = film;
    }

    public Salle getSalle() {
        return salle;
    }

    public void setSalle(Salle salle) {
        this.salle = salle;
    }

    public LocalDate getDateSeance() {
        return dateSeance;
    }

    public void setDateSeance(LocalDate dateSeance) {
        this.dateSeance = dateSeance;
    }

    public LocalTime getHeureDebut() {
        return heureDebut;
    }

    public void setHeureDebut(LocalTime heureDebut) {
        this.heureDebut = heureDebut;
    }

    public LocalTime getHeureFin() {
        return heureFin;
    }

    public void setHeureFin(LocalTime heureFin) {
        this.heureFin = heureFin;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "Seance{" +
                "id=" + id +
                ", film=" + (film != null ? film.getTitre() : "null") +
                ", salle=" + (salle != null ? salle.getNom() : "null") +
                ", dateSeance=" + dateSeance +
                ", heureDebut=" + heureDebut +
                ", heureFin=" + heureFin +
                ", createdAt=" + createdAt +
                '}';
    }
}
