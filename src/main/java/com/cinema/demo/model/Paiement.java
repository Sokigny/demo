package com.cinema.demo.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "paiements")
public class Paiement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private Integer montant;

    @ManyToOne
    @JoinColumn(name = "societe_id", nullable = false)
    private Societe societe;

    @OneToMany(mappedBy = "paiement", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PaiementDetail> details = new ArrayList<>();

    @Column(name = "date_paiement", nullable = false)
    private LocalDate datePaiement;

    // Constructors
    public Paiement() {
    }

    public Paiement(Integer montant, Societe societe, LocalDate datePaiement) {
        this.montant = montant;
        this.societe = societe;
        this.datePaiement = datePaiement;
    }

    // Getters and Setters
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

    public Societe getSociete() {
        return societe;
    }

    public void setSociete(Societe societe) {
        this.societe = societe;
    }

    public List<PaiementDetail> getDetails() {
        return details;
    }

    public void setDetails(List<PaiementDetail> details) {
        this.details = details;
    }

    public void addDetail(PaiementDetail detail) {
        details.add(detail);
        detail.setPaiement(this);
    }

    public void removeDetail(PaiementDetail detail) {
        details.remove(detail);
        detail.setPaiement(null);
    }

    public LocalDate getDatePaiement() {
        return datePaiement;
    }

    public void setDatePaiement(LocalDate datePaiement) {
        this.datePaiement = datePaiement;
    }
}
