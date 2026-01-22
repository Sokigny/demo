package com.cinema.demo.model;

import jakarta.persistence.*;

@Entity
@Table(name = "type_siege")
public class TypeSiege {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @Column(nullable = false)
    private String nom;
    
    @Column(nullable = false)
    private Integer prix;

    // Constructeurs
    public TypeSiege() {
    }

    public TypeSiege(String nom) {
        this.nom = nom;
    }

    public TypeSiege(String nom, Integer prix) {
        this.nom = nom;
        this.prix = prix;
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

    public Integer getPrix() {
        return prix;
    }

    public void setPrix(Integer prix) {
        this.prix = prix;
    }

    @Override
    public String toString() {
        return "TypeSiege{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                ", prix=" + prix +
                '}';
    }
}
