-- Migration pour ajouter seance_id dans la table paiements

-- Ajouter la colonne seance_id
ALTER TABLE paiements ADD COLUMN seance_id INTEGER;

-- Ajouter la contrainte de clé étrangère
ALTER TABLE paiements 
ADD CONSTRAINT fk_paiement_seance 
FOREIGN KEY (seance_id) 
REFERENCES seances(id) 
ON DELETE SET NULL;

-- Créer un index pour améliorer les performances des requêtes
CREATE INDEX idx_paiements_seance_id ON paiements(seance_id);
CREATE INDEX idx_paiements_societe_seance ON paiements(societe_id, seance_id);
