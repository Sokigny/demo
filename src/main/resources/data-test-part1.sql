-- Script d'insertion de données de test pour la base de données Cinema
-- Exécuter les scripts dans l'ordre suivant:
-- 1. data-test-part1.sql (tables de base)
-- 2. data-test-part2.sql (tables avec relations)

-- ==================================================
-- PART 1: Tables de base sans dépendances
-- ==================================================

-- Nettoyage des données existantes (optionnel - décommenter si nécessaire)
-- TRUNCATE TABLE seance_tarifs CASCADE;
-- TRUNCATE TABLE billets CASCADE;
-- TRUNCATE TABLE reservations CASCADE;
-- TRUNCATE TABLE seances CASCADE;
-- TRUNCATE TABLE films CASCADE;
-- TRUNCATE TABLE sieges CASCADE;
-- TRUNCATE TABLE type_siege CASCADE;
-- TRUNCATE TABLE salles CASCADE;
-- TRUNCATE TABLE cinemas CASCADE;
-- TRUNCATE TABLE clients CASCADE;
-- TRUNCATE TABLE users CASCADE;

-- ==================================================
-- INSERTION DES CLIENTS
-- ==================================================
INSERT INTO clients (nom, prenom, email, telephone, created_at) VALUES
('Rakoto', 'Jean', 'jean.rakoto@gmail.com', '0340123456', CURRENT_TIMESTAMP),
('Rasoa', 'Marie', 'marie.rasoa@yahoo.fr', '0341234567', CURRENT_TIMESTAMP),
('Randria', 'Paul', 'paul.randria@outlook.com', '0342345678', CURRENT_TIMESTAMP),
('Rasolofo', 'Sophie', 'sophie.rasolofo@gmail.com', '0343456789', CURRENT_TIMESTAMP),
('Andrianina', 'Luc', 'luc.andrianina@hotmail.com', '0344567890', CURRENT_TIMESTAMP);

-- ==================================================
-- INSERTION DES USERS (si nécessaire)
-- ==================================================
INSERT INTO users (nom, prenom, email, password) VALUES
('Admin', 'System', 'admin@cinema.com', 'password123'),
('Manager', 'Cinema', 'manager@cinema.com', 'password123');

-- ==================================================
-- INSERTION DES CINEMAS
-- ==================================================
INSERT INTO cinemas (nom, adresse) VALUES
('Cinéma Majestic', '67 Avenue de l''Indépendance, Antananarivo'),
('Cinéma Rex', 'Boulevard Rainilaiarivony, Antsirabe'),
('Cinéma Palace', 'Rue Colbert, Toliara'),
('Cinéma Roxy', 'Avenue du Général de Gaulle, Mahajanga');

-- ==================================================
-- INSERTION DES SALLES
-- ==================================================
-- Salles du Cinéma Majestic (ID=1)
INSERT INTO salles (cinema_id, nom) VALUES
(1, 'Salle Premium 1'),
(1, 'Salle VIP 2'),
(1, 'Salle Standard 3');

-- Salles du Cinéma Rex (ID=2)
INSERT INTO salles (cinema_id, nom) VALUES
(2, 'Salle Grande 1'),
(2, 'Salle Petite 2');

-- Salles du Cinéma Palace (ID=3)
INSERT INTO salles (cinema_id, nom) VALUES
(3, 'Salle A'),
(3, 'Salle B');

-- Salles du Cinéma Roxy (ID=4)
INSERT INTO salles (cinema_id, nom) VALUES
(4, 'Salle Principale');

-- ==================================================
-- INSERTION DES TYPES DE SIEGE
-- ==================================================
INSERT INTO type_siege (nom) VALUES
('Standard'),
('VIP'),
('Premium'),
('Couple');

-- ==================================================
-- INSERTION DES FILMS
-- ==================================================
INSERT INTO films (titre) VALUES
('Avatar: La Voie de l''Eau'),
('Top Gun: Maverick'),
('Black Panther: Wakanda Forever'),
('The Batman'),
('Spider-Man: No Way Home'),
('Jurassic World: Le Monde d''après'),
('Doctor Strange 2'),
('Thor: Love and Thunder'),
('Minions 2'),
('Elvis');

SELECT 'Part 1 completed - Tables de base créées avec succès!' as status;

SELECT id, film_id, date_seance, heure_debut, heure_fin 
FROM seances 
ORDER BY date_seance, heure_debut;

SELECT f.titre, s.date_seance, s.heure_debut, s.heure_fin 
FROM seances s 
JOIN films f ON s.film_id = f.id 
WHERE f.titre LIKE '%Avatar%' 
  AND s.date_seance = '2026-01-09';