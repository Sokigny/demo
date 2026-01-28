package com.cinema.demo.model;

import jakarta.persistence.*;

@Entity
@Table(name = "tarif")
public class Tarif {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @ManyToOne
    @JoinColumn(name = "typePersonne_id", nullable = false)
    private TypePersonne typePersonne;
    
    @ManyToOne
    @JoinColumn(name = "typeSiege_id", nullable = false)
    private TypeSiege typeSiege;
    
    @Column(nullable = false)
    private Integer montant;
    
    @Column
    private Double pourcentage;

    // Constructeurs
    public Tarif() {
    }

    public Tarif(TypePersonne typePersonne, TypeSiege typeSiege, Integer montant, Double pourcentage) {
        this.typePersonne = typePersonne;
        this.typeSiege = typeSiege;
        this.montant = montant;
        this.pourcentage = pourcentage;
    }

    // Getters et Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public TypePersonne getTypePersonne() {
        return typePersonne;
    }

    public void setTypePersonne(TypePersonne typePersonne) {
        this.typePersonne = typePersonne;
    }

    public TypeSiege getTypeSiege() {
        return typeSiege;
    }

    public void setTypeSiege(TypeSiege typeSiege) {
        this.typeSiege = typeSiege;
    }

    public Integer getMontant() {
        if (pourcentage == null || pourcentage == 0) {
            return montant;
        } else {
            return (int) (montant * pourcentage / 100);
        }
    }

    public void setMontant(Integer montant) {
        this.montant = montant;
    }

    public Double getPourcentage() {
        return pourcentage;
    }

    public void setPourcentage(Double pourcentage) {
        this.pourcentage = pourcentage;
    }

    @Override
    public String toString() {
        return "Tarif{" +
                "id=" + id +
                ", typePersonne=" + (typePersonne != null ? typePersonne.getNom() : "null") +
                ", typeSiege=" + (typeSiege != null ? typeSiege.getNom() : "null") +
                ", montant=" + montant +
                ", pourcentage=" + pourcentage +
                '}';
    }
}
