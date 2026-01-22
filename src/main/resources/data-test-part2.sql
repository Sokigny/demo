-- Script d'insertion de données de test - PART 2
-- Exécuter APRÈS data-test-part1.sql

-- ==================================================
-- INSERTION DES SIEGES
-- ==================================================
-- Salle Premium 1 (salle_id=1) - 30 sièges
INSERT INTO sieges (salle_id, rangee, numero, type_siege_id, etat) VALUES
(1, 'A', 1, 3, true), (1, 'A', 2, 3, true), (1, 'A', 3, 3, true), (1, 'A', 4, 3, true), (1, 'A', 5, 3, true),
(1, 'B', 1, 2, true), (1, 'B', 2, 2, true), (1, 'B', 3, 2, true), (1, 'B', 4, 2, true), (1, 'B', 5, 2, true),
(1, 'C', 1, 1, true), (1, 'C', 2, 1, true), (1, 'C', 3, 1, true), (1, 'C', 4, 1, true), (1, 'C', 5, 1, true);

-- Salle VIP 2 (salle_id=2) - 20 sièges VIP et Couple
INSERT INTO sieges (salle_id, rangee, numero, type_siege_id, etat) VALUES
(2, 'A', 1, 2, true), (2, 'A', 2, 2, true), (2, 'A', 3, 4, true), (2, 'A', 4, 4, true),
(2, 'B', 1, 2, true), (2, 'B', 2, 2, true), (2, 'B', 3, 2, true), (2, 'B', 4, 2, true);

-- Salle Standard 3 (salle_id=3) - 40 sièges Standard
INSERT INTO sieges (salle_id, rangee, numero, type_siege_id, etat) VALUES
(3, 'A', 1, 1, true), (3, 'A', 2, 1, true), (3, 'A', 3, 1, true), (3, 'A', 4, 1, true), (3, 'A', 5, 1, true),
(3, 'B', 1, 1, true), (3, 'B', 2, 1, true), (3, 'B', 3, 1, true), (3, 'B', 4, 1, true), (3, 'B', 5, 1, true),
(3, 'C', 1, 1, true), (3, 'C', 2, 1, true), (3, 'C', 3, 1, true), (3, 'C', 4, 1, true), (3, 'C', 5, 1, true);

-- Salle Grande 1 du Rex (salle_id=4)
INSERT INTO sieges (salle_id, rangee, numero, type_siege_id, etat) VALUES
(4, 'A', 1, 1, true), (4, 'A', 2, 1, true), (4, 'A', 3, 1, true), (4, 'A', 4, 1, true),
(4, 'B', 1, 1, true), (4, 'B', 2, 1, true), (4, 'B', 3, 1, true), (4, 'B', 4, 1, true);

-- ==================================================
-- INSERTION DES SEANCES
-- ==================================================
-- Séances d'aujourd'hui et demain
INSERT INTO seances (film_id, salle_id, date_seance, heure_debut, heure_fin, created_at) VALUES
(1, 1, CURRENT_DATE, '10:00:00', '13:00:00', CURRENT_TIMESTAMP),
(1, 1, CURRENT_DATE, '14:00:00', '17:00:00', CURRENT_TIMESTAMP),
(1, 1, CURRENT_DATE, '18:00:00', '21:00:00', CURRENT_TIMESTAMP),
(2, 2, CURRENT_DATE, '10:30:00', '12:30:00', CURRENT_TIMESTAMP),
(2, 2, CURRENT_DATE, '15:00:00', '17:00:00', CURRENT_TIMESTAMP),
(2, 2, CURRENT_DATE, '20:00:00', '22:00:00', CURRENT_TIMESTAMP),
(3, 3, CURRENT_DATE, '11:00:00', '13:30:00', CURRENT_TIMESTAMP),
(3, 3, CURRENT_DATE, '16:00:00', '18:30:00', CURRENT_TIMESTAMP),
(3, 3, CURRENT_DATE, '19:00:00', '21:30:00', CURRENT_TIMESTAMP),
(4, 4, CURRENT_DATE, '09:00:00', '12:00:00', CURRENT_TIMESTAMP),
(4, 4, CURRENT_DATE, '13:00:00', '16:00:00', CURRENT_TIMESTAMP),
(5, 1, CURRENT_DATE + 1, '10:00:00', '12:30:00', CURRENT_TIMESTAMP),
(5, 1, CURRENT_DATE + 1, '14:00:00', '16:30:00', CURRENT_TIMESTAMP),
(5, 1, CURRENT_DATE + 1, '18:00:00', '20:30:00', CURRENT_TIMESTAMP),
(6, 2, CURRENT_DATE + 1, '11:00:00', '13:30:00', CURRENT_TIMESTAMP),
(6, 2, CURRENT_DATE + 1, '15:00:00', '17:30:00', CURRENT_TIMESTAMP),
(7, 3, CURRENT_DATE + 1, '10:00:00', '12:00:00', CURRENT_TIMESTAMP),
(7, 3, CURRENT_DATE + 1, '16:00:00', '18:00:00', CURRENT_TIMESTAMP);

-- ==================================================
-- INSERTION DES TARIFS PAR SEANCE
-- ==================================================
-- Tarifs pour les séances d'Avatar (seances 1, 2, 3)
INSERT INTO seance_tarifs (seance_id, montant, type_siege_id) VALUES
(1, 5000, 1), (1, 8000, 2), (1, 10000, 3),
(2, 5000, 1), (2, 8000, 2), (2, 10000, 3),
(3, 5000, 1), (3, 8000, 2), (3, 10000, 3);

-- Tarifs pour Top Gun (seances 4, 5, 6)
INSERT INTO seance_tarifs (seance_id, montant, type_siege_id) VALUES
(4, 7000, 2), (4, 12000, 4),
(5, 7000, 2), (5, 12000, 4),
(6, 7000, 2), (6, 12000, 4);

-- Tarifs pour Black Panther (seances 7, 8, 9)
INSERT INTO seance_tarifs (seance_id, montant, type_siege_id) VALUES
(7, 4000, 1), (8, 4000, 1), (9, 4000, 1);

-- Tarifs pour The Batman (seances 10, 11)
INSERT INTO seance_tarifs (seance_id, montant, type_siege_id) VALUES
(10, 4500, 1), (11, 4500, 1);

-- Tarifs pour Spider-Man (seances 12, 13, 14)
INSERT INTO seance_tarifs (seance_id, montant, type_siege_id) VALUES
(12, 5000, 1), (12, 8000, 2), (12, 10000, 3),
(13, 5000, 1), (13, 8000, 2), (13, 10000, 3),
(14, 5000, 1), (14, 8000, 2), (14, 10000, 3);

-- ==================================================
-- INSERTION DES RESERVATIONS (sans billet_id d'abord)
-- ==================================================
INSERT INTO reservations (client_id, billet_id, date_reservation, status) VALUES
(1, NULL, CURRENT_TIMESTAMP, 1),
(2, NULL, CURRENT_TIMESTAMP, 1),
(3, NULL, CURRENT_TIMESTAMP, 1),
(4, NULL, CURRENT_TIMESTAMP, 0),
(5, NULL, CURRENT_TIMESTAMP, 1);

-- ==================================================
-- INSERTION DES BILLETS
-- ==================================================
INSERT INTO billets (reservation_id, siege_id, seances_id, statut) VALUES
(1, 1, 1, 1),
(1, 2, 1, 1),
(2, 9, 4, 1),
(3, 16, 8, 1),
(3, 17, 8, 1),
(4, 24, 10, 0),
(5, 1, 12, 1),
(5, 2, 12, 1),
(5, 3, 12, 1);

-- ==================================================
-- MISE A JOUR DES RESERVATIONS avec billet_id
-- ==================================================
-- Lier chaque réservation à son premier billet
UPDATE reservations SET billet_id = 1 WHERE id = 1;
UPDATE reservations SET billet_id = 3 WHERE id = 2;
UPDATE reservations SET billet_id = 4 WHERE id = 3;
UPDATE reservations SET billet_id = 6 WHERE id = 4;
UPDATE reservations SET billet_id = 7 WHERE id = 5;

SELECT 'Part 2 completed - Toutes les données de test ont été insérées avec succès!' as status;
SELECT 
    (SELECT COUNT(*) FROM clients) as clients,
    (SELECT COUNT(*) FROM cinemas) as cinemas,
    (SELECT COUNT(*) FROM salles) as salles,
    (SELECT COUNT(*) FROM type_siege) as types_siege,
    (SELECT COUNT(*) FROM sieges) as sieges,
    (SELECT COUNT(*) FROM films) as films,
    (SELECT COUNT(*) FROM seances) as seances,
    (SELECT COUNT(*) FROM seance_tarifs) as tarifs,
    (SELECT COUNT(*) FROM reservations) as reservations,
    (SELECT COUNT(*) FROM billets) as billets;
