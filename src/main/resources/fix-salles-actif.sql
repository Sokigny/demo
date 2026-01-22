-- Script pour mettre à jour les salles existantes avec actif = true
UPDATE salles SET actif = true WHERE actif IS NULL;

-- Rendre la colonne NOT NULL avec valeur par défaut
ALTER TABLE salles ALTER COLUMN actif SET DEFAULT true;
ALTER TABLE salles ALTER COLUMN actif SET NOT NULL;
