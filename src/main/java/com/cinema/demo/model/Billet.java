package com.cinema.demo.model;

import jakarta.persistence.*;

@Entity
@Table(name = "billets")
public class Billet {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @ManyToOne
    @JoinColumn(name = "reservation_id", nullable = false)
    private Reservation reservation;
    
    @ManyToOne
    @JoinColumn(name = "siege_id", nullable = false)
    private Siege siege;
    
    @ManyToOne
    @JoinColumn(name = "seances_id", nullable = false)
    private Seance seance;
    
    @ManyToOne
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;
    
    @ManyToOne
    @JoinColumn(name = "remise_id")
    private Remise remise;
    
    @Column(nullable = false)
    private Integer prix;
    
    @Column(nullable = false)
    private Integer statut;

    // Constructeurs
    public Billet() {
    }

    public Billet(Reservation reservation, Siege siege, Seance seance, Integer statut) {
        this.reservation = reservation;
        this.siege = siege;
        this.seance = seance;
        this.statut = statut;
    }

    public Billet(Reservation reservation, Siege siege, Seance seance, Client client, Integer prix, Integer statut) {
        this.reservation = reservation;
        this.siege = siege;
        this.seance = seance;
        this.client = client;
        this.prix = prix;
        this.statut = statut;
    }

    public Billet(Reservation reservation, Siege siege, Seance seance, Client client, Remise remise, Integer prix, Integer statut) {
        this.reservation = reservation;
        this.siege = siege;
        this.seance = seance;
        this.client = client;
        this.remise = remise;
        this.prix = prix;
        this.statut = statut;
    }

    // Getters et Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Reservation getReservation() {
        return reservation;
    }

    public void setReservation(Reservation reservation) {
        this.reservation = reservation;
    }

    public Siege getSiege() {
        return siege;
    }

    public void setSiege(Siege siege) {
        this.siege = siege;
    }

    public Seance getSeance() {
        return seance;
    }

    public void setSeance(Seance seance) {
        this.seance = seance;
    }

    public Integer getStatut() {
        return statut;
    }

    public void setStatut(Integer statut) {
        this.statut = statut;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Integer getPrix() {
        return prix;
    }

    public void setPrix(Integer prix) {
        this.prix = prix;
    }

    public Remise getRemise() {
        return remise;
    }

    public void setRemise(Remise remise) {
        this.remise = remise;
    }

    @Override
    public String toString() {
        return "Billet{" +
                "id=" + id +
                ", reservation=" + (reservation != null ? reservation.getId() : "null") +
                ", siege=" + (siege != null ? siege.getId() : "null") +
                ", seance=" + (seance != null ? seance.getId() : "null") +
                ", client=" + (client != null ? client.getNom() + " " + client.getPrenom() : "null") +
                ", remise=" + (remise != null ? remise.getId() : "null") +
                ", prix=" + prix +
                ", statut=" + statut +
                '}';
    }
}
