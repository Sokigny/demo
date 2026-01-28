package com.cinema.demo.model;

import jakarta.persistence.*;

@Entity
@Table(name = "type_personne")
public class TypePersonne {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @Column(nullable = false)
    private String nom;
    
    @Column(name = "age_min")
    private Integer ageMin;
    
    @Column(name = "age_max")
    private Integer ageMax;

    // Constructeurs
    public TypePersonne() {
    }

    public TypePersonne(String nom) {
        this.nom = nom;
    }
    
    public TypePersonne(String nom, Integer ageMin, Integer ageMax) {
        this.nom = nom;
        this.ageMin = ageMin;
        this.ageMax = ageMax;
    }

    // Getters et Setters
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

    public Integer getAgeMin() {
        return ageMin;
    }

    public void setAgeMin(Integer ageMin) {
        this.ageMin = ageMin;
    }

    public Integer getAgeMax() {
        return ageMax;
    }

    public void setAgeMax(Integer ageMax) {
        this.ageMax = ageMax;
    }

    @Override
    public String toString() {
        return "TypePersonne{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                ", ageMin=" + ageMin +
                ", ageMax=" + ageMax +
                '}';
    }
}
