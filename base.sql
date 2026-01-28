-- Script de création de la base de données pour le système de cinéma

-- Table des cinémas
CREATE TABLE cinemas (
    id SERIAL PRIMARY KEY,
    nom VARCHAR(255) NOT NULL,
    adresse VARCHAR(255) NOT NULL
);

-- Table des salles
CREATE TABLE salles (
    id SERIAL PRIMARY KEY,
    cinema_id INTEGER NOT NULL,
    nom VARCHAR(255) NOT NULL,
    FOREIGN KEY (cinema_id) REFERENCES cinemas(id)
);

-- Table des films
CREATE TABLE films (
    id SERIAL PRIMARY KEY,
    titre VARCHAR(255) NOT NULL
);

-- Table des séances
CREATE TABLE seances (
    id SERIAL PRIMARY KEY,
    film_id INTEGER NOT NULL,
    salle_id INTEGER NOT NULL,
    date_seance DATE NOT NULL,
    heure_debut TIME NOT NULL,
    heure_fin TIME NOT NULL,
    created_at TIMESTAMP NOT NULL,
    FOREIGN KEY (film_id) REFERENCES films(id),
    FOREIGN KEY (salle_id) REFERENCES salles(id)
);

-- Table des types de sièges
CREATE TABLE type_siege (
    id SERIAL PRIMARY KEY,
    nom VARCHAR(255) NOT NULL
);

-- Table des types de personne
CREATE TABLE type_personne (
    id SERIAL PRIMARY KEY,
    nom VARCHAR(255) NOT NULL,
    age_min INTEGER,
    age_max INTEGER
);

-- Table des tarifs par type de personne
CREATE TABLE tarif (
    id SERIAL PRIMARY KEY,
    typePersonne_id INTEGER NOT NULL,
    typeSiege_id INTEGER NOT NULL,
    montant INTEGER NOT NULL,
    pourcentage DECIMAL(5,2),
    FOREIGN KEY (typePersonne_id) REFERENCES type_personne(id),
    FOREIGN KEY (typeSiege_id) REFERENCES type_siege(id)
);

-- Table des tarifs par séance et type de siège
CREATE TABLE seance_tarifs (
    id SERIAL PRIMARY KEY,
    seance_id INTEGER NOT NULL,
    typeSiege_id INTEGER NOT NULL,
    montant INTEGER NOT NULL,
    FOREIGN KEY (seance_id) REFERENCES seances(id),
    FOREIGN KEY (typeSiege_id) REFERENCES type_siege(id)
);

-- Table des sièges
CREATE TABLE sieges (
    id SERIAL PRIMARY KEY,
    salle_id INTEGER NOT NULL,
    rangee VARCHAR(255) NOT NULL,
    numero INTEGER NOT NULL,
    typeSiege_id INTEGER NOT NULL,
    etat BOOLEAN NOT NULL,
    FOREIGN KEY (salle_id) REFERENCES salles(id),
    FOREIGN KEY (typeSiege_id) REFERENCES type_siege(id)
);

-- Table des clients
CREATE TABLE clients (
    id SERIAL PRIMARY KEY,
    nom VARCHAR(255) NOT NULL,
    prenom VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    telephone VARCHAR(255) NOT NULL,
    created_at TIMESTAMP NOT NULL
);

-- Table des réservations
CREATE TABLE reservations (
    id SERIAL PRIMARY KEY,
    client_id INTEGER NOT NULL,
    billet_id INTEGER,
    date_reservation TIMESTAMP NOT NULL,
    status INTEGER NOT NULL,
    FOREIGN KEY (client_id) REFERENCES clients(id)
);

-- Table des billets
CREATE TABLE billets (
    id SERIAL PRIMARY KEY,
    reservation_id INTEGER NOT NULL,
    siege_id INTEGER NOT NULL,
    seances_id INTEGER NOT NULL,
    statut INTEGER NOT NULL,
    FOREIGN KEY (reservation_id) REFERENCES reservations(id),
    FOREIGN KEY (siege_id) REFERENCES sieges(id),
    FOREIGN KEY (seances_id) REFERENCES seances(id)
);

-- Table des utilisateurs (pour l'administration)
CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    nom VARCHAR(255) NOT NULL,
    prenom VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL
);

-- Ajout de la contrainte de clé étrangère pour billet_id dans reservations
ALTER TABLE reservations 
ADD CONSTRAINT fk_reservations_billet 


UPDATE salles SET actif = true WHERE actif IS NULL;
ALTER TABLE salles ALTER COLUMN actif SET DEFAULT true;
ALTER TABLE salles ALTER COLUMN actif SET NOT NULL;