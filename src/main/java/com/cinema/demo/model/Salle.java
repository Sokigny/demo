package com.cinema.demo.model;

import jakarta.persistence.*;

@Entity
@Table(name = "salles")
public class Salle {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @ManyToOne
    @JoinColumn(name = "cinema_id", nullable = false)
    private Cinema cinema;
    
    @Column(nullable = false)
    private String nom;
    
    @Column
    private Integer capacite;
    
    @Column
    private Boolean actif = true;

    // Constructeurs
    public Salle() {
    }
    
    @PrePersist
    protected void onCreate() {
        if (actif == null) {
            actif = true;
        }
    }

    public Salle(Cinema cinema, String nom) {
        this.cinema = cinema;
        this.nom = nom;
    }

    // Getters et Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Cinema getCinema() {
        return cinema;
    }

    public void setCinema(Cinema cinema) {
        this.cinema = cinema;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }
    
    public Integer getCapacite() {
        return capacite;
    }
    
    public void setCapacite(Integer capacite) {
        this.capacite = capacite;
    }
    
    public Boolean getActif() {
        return actif;
    }
    
    public void setActif(Boolean actif) {
        this.actif = actif;
    }

    @Override
    public String toString() {
        return "Salle{" +
                "id=" + id +
                ", cinema=" + (cinema != null ? cinema.getNom() : "null") +
                ", nom='" + nom + '\'' +
                '}';
    }
}
