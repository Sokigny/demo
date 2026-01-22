# Guide d'exécution des scripts de données de test

## Prérequis
- PostgreSQL installé et en cours d'exécution
- Base de données créée (par exemple : `cinema_db`)
- Schéma de tables créé (via Spring Boot JPA ou scripts SQL)

## Instructions d'exécution

### Option 1: Via psql (ligne de commande)

```bash
# Se connecter à la base de données
psql -U votre_user -d cinema_db

# Exécuter les scripts dans l'ordre
\i E:/S5/MdmBaovola/demo/demo/src/main/resources/data-test-part1.sql
\i E:/S5/MdmBaovola/demo/demo/src/main/resources/data-test-part2.sql
```

### Option 2: Via pgAdmin

1. Ouvrir pgAdmin
2. Se connecter à votre serveur PostgreSQL
3. Sélectionner la base de données `cinema_db`
4. Ouvrir Query Tool (Outils > Query Tool)
5. Ouvrir et exécuter `data-test-part1.sql`
6. Ouvrir et exécuter `data-test-part2.sql`

### Option 3: Exécution directe en PowerShell

```powershell
# Remplacer les valeurs selon votre configuration
$env:PGPASSWORD='votre_password'
psql -U votre_user -d cinema_db -f "E:\S5\MdmBaovola\demo\demo\src\main\resources\data-test-part1.sql"
psql -U votre_user -d cinema_db -f "E:\S5\MdmBaovola\demo\demo\src\main\resources\data-test-part2.sql"
```

## Contenu des données de test

### Part 1 (Tables de base):
- **5 clients** avec emails et téléphones
- **2 users** (admin et manager)
- **4 cinémas** (Majestic, Rex, Palace, Roxy)
- **8 salles** réparties dans les cinémas
- **4 types de sièges** (Standard, VIP, Premium, Couple)
- **10 films** populaires

### Part 2 (Relations et données complexes):
- **Plus de 50 sièges** répartis dans différentes salles
- **17 séances** (aujourd'hui et demain)
- **Tarifs variés** selon le type de siège et la séance
- **5 réservations** avec différents statuts
- **9 billets** liés aux réservations

## Vérification

Après l'exécution, vous devriez voir un résumé avec le nombre d'enregistrements dans chaque table.

## Nettoyage (si nécessaire)

Si vous voulez supprimer toutes les données de test et recommencer:

```sql
TRUNCATE TABLE seance_tarifs CASCADE;
TRUNCATE TABLE billets CASCADE;
TRUNCATE TABLE reservations CASCADE;
TRUNCATE TABLE seances CASCADE;
TRUNCATE TABLE films CASCADE;
TRUNCATE TABLE sieges CASCADE;
TRUNCATE TABLE type_siege CASCADE;
TRUNCATE TABLE salles CASCADE;
TRUNCATE TABLE cinemas CASCADE;
TRUNCATE TABLE clients CASCADE;
TRUNCATE TABLE users CASCADE;
```

## Notes importantes

1. **Ordre d'exécution**: Exécuter TOUJOURS Part 1 avant Part 2
2. **Relations circulaires**: Les réservations et billets ont une relation circulaire, c'est pourquoi Part 2 fait d'abord les insertions puis les mises à jour
3. **Dates dynamiques**: Les scripts utilisent `CURRENT_DATE` et `CURRENT_TIMESTAMP` pour avoir des données actuelles
4. **IDs auto-incrémentés**: Les IDs sont générés automatiquement, donc les valeurs peuvent varier

## Dépannage

**Erreur de foreign key**: Assurez-vous que Part 1 a été exécuté avec succès avant Part 2

**Erreur de contrainte unique**: La base contient déjà des données, utilisez TRUNCATE pour nettoyer

**Erreur de connexion**: Vérifiez vos identifiants PostgreSQL dans application.properties
