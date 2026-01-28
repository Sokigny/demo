package com.cinema.demo.model;

import jakarta.persistence.*;

@Entity
@Table(name = "remises")
public class Remise {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @Column(nullable = false)
    private Integer montant;
    
    @ManyToOne
    @JoinColumn(name = "type_siege_id", nullable = false)
    private TypeSiege typeSiege;
    
    @Column(name = "age_min", nullable = false)
    private Integer ageMin;
    
    @Column(name = "age_max", nullable = false)
    private Integer ageMax;

    // Constructeurs
    public Remise() {
    }

    public Remise(Integer montant, TypeSiege typeSiege, Integer ageMin, Integer ageMax) {
        this.montant = montant;
        this.typeSiege = typeSiege;
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

    public Integer getMontant() {
        return montant;
    }

    public void setMontant(Integer montant) {
        this.montant = montant;
    }

    public TypeSiege getTypeSiege() {
        return typeSiege;
    }

    public void setTypeSiege(TypeSiege typeSiege) {
        this.typeSiege = typeSiege;
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

    /**
     * Vérifie si la remise est applicable pour un âge donné
     * @param age l'âge à vérifier
     * @return true si l'âge est dans la tranche [ageMin, ageMax]
     */
    public boolean isApplicableForAge(Integer age) {
        if (age == null) {
            return false;
        }
        return age >= ageMin && age <= ageMax;
    }

    /**
     * Calcule le prix final après application de la remise
     * @param prixOriginal le prix original
     * @return le prix après remise
     */
    public Integer calculerPrixApresRemise(Integer prixOriginal) {
        if (prixOriginal == null || montant == null) {
            return prixOriginal;
        }
        int prixFinal = prixOriginal - (prixOriginal * montant / 100);
        // if(montant == 33){
        //     prixFinal = 20000;
        // }
        // if(montant == 25){
        //     prixFinal = 30000;
        // }
        // if(montant == 10){
        //     prixFinal = 45000;
        // }
        return Math.max(0, prixFinal); // Le prix ne peut pas être négatif
    }

    @Override
    public String toString() {
        return "Remise{" +
                "id=" + id +
                ", montant=" + montant +
                ", typeSiege=" + (typeSiege != null ? typeSiege.getNom() : "null") +
                ", ageMin=" + ageMin +
                ", ageMax=" + ageMax +
                '}';
    }
}
