package com.cinema.demo.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "publicite")
public class Publicite {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @ManyToOne
    @JoinColumn(name = "seance_id")
    private Seance seance;
    
    @ManyToOne
    @JoinColumn(name = "societe_id")
    private Societe societe;
    
    @Column(name = "longueur")
    private Double longueur;
    
    @Column(name = "nombre")
    private Integer nombre;
    
    @Column(name = "date")
    private LocalDate date;
    
    public Publicite() {
    }
    
    public Publicite(Seance seance, Societe societe, Double longueur, Integer nombre, LocalDate date) {
        this.seance = seance;
        this.societe = societe;
        this.longueur = longueur;
        this.nombre = nombre;
        this.date = date;
    }
    
    public Integer getId() {
        return id;
    }
    
    public void setId(Integer id) {
        this.id = id;
    }
    
    public Seance getSeance() {
        return seance;
    }
    
    public void setSeance(Seance seance) {
        this.seance = seance;
    }
    
    public Societe getSociete() {
        return societe;
    }
    
    public void setSociete(Societe societe) {
        this.societe = societe;
    }
    
    public Double getLongueur() {
        return longueur;
    }
    
    public void setLongueur(Double longueur) {
        this.longueur = longueur;
    }
    
    public Integer getNombre() {
        return nombre;
    }
    
    public void setNombre(Integer nombre) {
        this.nombre = nombre;
    }
    
    public LocalDate getDate() {
        return date;
    }
    
    public void setDate(LocalDate date) {
        this.date = date;
    }
}
