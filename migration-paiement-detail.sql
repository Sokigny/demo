-- Migration pour créer la table paiement_detail et modifier paiements

-- Créer la table paiement_detail
CREATE TABLE paiement_detail (
    id SERIAL PRIMARY KEY,
    paiement_id INTEGER NOT NULL,
    seance_id INTEGER NOT NULL,
    montant DOUBLE PRECISION NOT NULL,
    CONSTRAINT fk_paiement_detail_paiement FOREIGN KEY (paiement_id) REFERENCES paiements(id) ON DELETE CASCADE,
    CONSTRAINT fk_paiement_detail_seance FOREIGN KEY (seance_id) REFERENCES seances(id) ON DELETE CASCADE
);

-- Créer des index pour améliorer les performances
CREATE INDEX idx_paiement_detail_paiement_id ON paiement_detail(paiement_id);
CREATE INDEX idx_paiement_detail_seance_id ON paiement_detail(seance_id);
CREATE INDEX idx_paiement_detail_paiement_seance ON paiement_detail(paiement_id, seance_id);

-- Supprimer la colonne seance_id de la table paiements
ALTER TABLE paiements DROP CONSTRAINT IF EXISTS fk_paiement_seance;
ALTER TABLE paiements DROP COLUMN IF EXISTS seance_id;
