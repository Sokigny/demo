package com.cinema.demo.model;

import jakarta.persistence.*;

@Entity
@Table(name = "societe")
public class Societe {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @Column(name = "nom")
    private String nom;
    
    @Column(name = "montant_par_min")
    private Double montantParMin;
    
    public Societe() {
    }
    
    public Societe(String nom, Double montantParMin) {
        this.nom = nom;
        this.montantParMin = montantParMin;
    }
    
    public Integer getId() {
        return id;
    }
    
    public void setId(Integer id) {
        this.id = id;
    }
    
    public String getNom() {
        return nom;
    }
    
    public void setNom(String nom) {
        this.nom = nom;
    }
    
    public Double getMontantParMin() {
        return montantParMin;
    }
    
    public void setMontantParMin(Double montantParMin) {
        this.montantParMin = montantParMin;
    }
}
