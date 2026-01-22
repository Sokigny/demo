-- Migration: Déplacer le prix de seance_tarifs vers type_siege
-- Date: 2026-01-16

-- Étape 1: Ajouter la colonne prix dans type_siege
ALTER TABLE type_siege ADD COLUMN IF NOT EXISTS prix INTEGER;

-- Étape 2: Mettre à jour les prix des types de sièges avec les valeurs des tarifs existants
-- (On prend le premier tarif trouvé pour chaque type de siège comme prix par défaut)
UPDATE type_siege ts
SET prix = (
    SELECT st.montant 
    FROM seance_tarifs st 
    WHERE st."typeSiege_id" = ts.id 
    LIMIT 1
)
WHERE prix IS NULL;

-- Étape 3: Définir un prix par défaut pour les types de sièges sans tarif
-- Standard: 5000 Ar, VIP: 10000 Ar, autres: 5000 Ar
UPDATE type_siege 
SET prix = CASE 
    WHEN LOWER(nom) LIKE '%vip%' THEN 10000
    WHEN LOWER(nom) LIKE '%premium%' THEN 8000
    ELSE 5000
END
WHERE prix IS NULL;

-- Étape 4: Rendre la colonne prix obligatoire (NOT NULL)
ALTER TABLE type_siege ALTER COLUMN prix SET NOT NULL;

-- Étape 5: Supprimer la colonne montant de seance_tarifs
-- (Les prix sont maintenant gérés au niveau du type de siège)
ALTER TABLE seance_tarifs DROP COLUMN IF EXISTS montant;

-- Vérification des résultats
SELECT id, nom, prix FROM type_siege ORDER BY id;

-- Afficher les tarifs par séance (prix vient maintenant de type_siege)
SELECT 
    st.id,
    st.seance_id,
    ts.nom as type_siege,
    ts.prix
FROM seance_tarifs st
JOIN type_siege ts ON ts.id = st."typeSiege_id"
ORDER BY st.seance_id, ts.nom;
