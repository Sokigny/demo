package com.cinema.demo.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "reservations")
public class Reservation {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @ManyToOne
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;
    
    @OneToOne
    @JoinColumn(name = "billet_id")
    private Billet billet;
    
    @Column(name = "date_reservation", nullable = false, updatable = false)
    private LocalDateTime dateReservation;
    
    @Column(nullable = false)
    private Integer status;

    // Constructeurs
    public Reservation() {
    }

    public Reservation(Client client, Billet billet, Integer status) {
        this.client = client;
        this.billet = billet;
        this.status = status;
    }

    @PrePersist
    protected void onCreate() {
        dateReservation = LocalDateTime.now();
    }

    // Getters et Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Billet getBillet() {
        return billet;
    }

    public void setBillet(Billet billet) {
        this.billet = billet;
    }

    public LocalDateTime getDateReservation() {
        return dateReservation;
    }

    public void setDateReservation(LocalDateTime dateReservation) {
        this.dateReservation = dateReservation;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Reservation{" +
                "id=" + id +
                ", client=" + (client != null ? client.getEmail() : "null") +
                ", billet=" + (billet != null ? billet.getId() : "null") +
                ", dateReservation=" + dateReservation +
                ", status=" + status +
                '}';
    }
}
