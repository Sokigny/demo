package com.cinema.demo.model;

import jakarta.persistence.*;

@Entity
@Table(name = "films")
public class Film {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @Column(nullable = false)
    private String titre;
    
    @Column(length = 1000)
    private String description;
    
    @Column
    private Integer duree; // durée en minutes
    
    @Column
    private String genre;
    
    @Column(name = "image_url")
    private String imageUrl;

    // Constructeurs
    public Film() {
    }

    public Film(String titre) {
        this.titre = titre;
    }
    
    public Film(String titre, String description, Integer duree, String genre, String imageUrl) {
        this.titre = titre;
        this.description = description;
        this.duree = duree;
        this.genre = genre;
        this.imageUrl = imageUrl;
    }

    // Getters et Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public Integer getDuree() {
        return duree;
    }
    
    public void setDuree(Integer duree) {
        this.duree = duree;
    }
    
    public String getGenre() {
        return genre;
    }
    
    public void setGenre(String genre) {
        this.genre = genre;
    }
    
    public String getImageUrl() {
        return imageUrl;
    }
    
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    @Override
    public String toString() {
        return "Film{" +
                "id=" + id +
                ", titre='" + titre + '\'' +
                ", genre='" + genre + '\'' +
                ", duree=" + duree +
                '}';
    }
}
