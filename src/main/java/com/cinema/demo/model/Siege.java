package com.cinema.demo.model;

import jakarta.persistence.*;

@Entity
@Table(name = "sieges")
public class Siege {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @ManyToOne
    @JoinColumn(name = "salle_id", nullable = false)
    private Salle salle;
    
    @Column(nullable = false)
    private String rangee;
    
    @Column(nullable = false)
    private Integer numero;
    
    @ManyToOne
    @JoinColumn(name = "typeSiege_id", nullable = false)
    private TypeSiege typeSiege;
    
    @Column(nullable = false)
    private Boolean etat;

    // Constructeurs
    public Siege() {
    }

    public Siege(Salle salle, String rangee, Integer numero, TypeSiege typeSiege, Boolean etat) {
        this.salle = salle;
        this.rangee = rangee;
        this.numero = numero;
        this.typeSiege = typeSiege;
        this.etat = etat;
    }

    // Getters et Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Salle getSalle() {
        return salle;
    }

    public void setSalle(Salle salle) {
        this.salle = salle;
    }

    public String getRangee() {
        return rangee;
    }

    public void setRangee(String rangee) {
        this.rangee = rangee;
    }

    public Integer getNumero() {
        return numero;
    }

    public void setNumero(Integer numero) {
        this.numero = numero;
    }

    public TypeSiege getTypeSiege() {
        return typeSiege;
    }

    public void setTypeSiege(TypeSiege typeSiege) {
        this.typeSiege = typeSiege;
    }

    public Boolean getEtat() {
        return etat;
    }

    public void setEtat(Boolean etat) {
        this.etat = etat;
    }

    @Override
    public String toString() {
        return "Siege{" +
                "id=" + id +
                ", salle=" + (salle != null ? salle.getNom() : "null") +
                ", rangee='" + rangee + '\'' +
                ", numero=" + numero +
                ", typeSiege=" + (typeSiege != null ? typeSiege.getNom() : "null") +
                ", etat=" + etat +
                '}';
    }
}
