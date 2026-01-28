package com.cinema.demo.service;

import com.cinema.demo.model.*;
import com.cinema.demo.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

@Service
public class SeanceService {

    @Autowired
    private SeanceRepository seanceRepository;

    @Autowired
    private FilmRepository filmRepository;

    @Autowired
    private SalleRepository salleRepository;

    @Autowired
    private SiegeRepository siegeRepository;

    @Autowired
    private BilletRepository billetRepository;

    @Autowired
    private TypeSiegeRepository typeSiegeRepository;

    @Autowired
    private PubliciteRepository publiciteRepository;

    @Autowired
    private PaiementDetailService paiementDetailService;

    // Create
    public Seance createSeance(Seance seance) {
        if (seance.getFilm() == null || seance.getFilm().getId() == null) {
            throw new RuntimeException("Le film est requis");
        }

        if (seance.getSalle() == null || seance.getSalle().getId() == null) {
            throw new RuntimeException("La salle est requise");
        }

        Film film = filmRepository.findById(seance.getFilm().getId())
                .orElseThrow(() -> new RuntimeException("Film non trouvé avec l'id: " + seance.getFilm().getId()));

        Salle salle = salleRepository.findById(seance.getSalle().getId())
                .orElseThrow(() -> new RuntimeException("Salle non trouvée avec l'id: " + seance.getSalle().getId()));

        seance.setFilm(film);
        seance.setSalle(salle);

        return seanceRepository.save(seance);
    }

    // Read - toutes les séances
    public List<Seance> getAllSeances() {
        return seanceRepository.findAll();
    }

    // Read - par ID
    public Optional<Seance> getSeanceById(Integer id) {
        return seanceRepository.findById(id);
    }

    // Read - par film
    public List<Seance> getSeancesByFilmId(Integer filmId) {
        return seanceRepository.findByFilmId(filmId);
    }

    // Read - par salle
    public List<Seance> getSeancesBySalleId(Integer salleId) {
        return seanceRepository.findBySalleId(salleId);
    }

    // Read - par date
    public List<Seance> getSeancesByDate(LocalDate dateSeance) {
        return seanceRepository.findByDateSeance(dateSeance);
    }

    // Read - par salle et date
    public List<Seance> getSeancesBySalleIdAndDate(Integer salleId, LocalDate dateSeance) {
        return seanceRepository.findBySalleIdAndDateSeance(salleId, dateSeance);
    }

    // Read - par film et date
    public List<Seance> getSeancesByFilmIdAndDate(Integer filmId, LocalDate dateSeance) {
        return seanceRepository.findByFilmIdAndDateSeance(filmId, dateSeance);
    }

    // Rechercher par nom de film
    public List<Seance> searchSeancesByFilmTitre(String nomFilm) {
        return seanceRepository.findByFilmTitreContaining(nomFilm);
    }

    // Rechercher les séances à une heure donnée (en cours ou qui commencent)
    public List<Seance> getSeancesAtTime(LocalDate date, LocalTime heure) {
        return seanceRepository.findSeancesAtTime(date, heure);
    }

    // Rechercher les séances par nom de film, date et heure
    public List<Seance> searchSeancesByFilmDateAndTime(String nomFilm, LocalDate date, LocalTime heure) {
        System.out.println("=== SERVICE: searchSeancesByFilmDateAndTime ===");
        System.out.println("  nomFilm: '" + nomFilm + "'");
        System.out.println("  date: " + date);
        System.out.println("  heure: " + heure);
        List<Seance> results = seanceRepository.findByFilmAndDateAndTime(nomFilm, date, heure);
        System.out.println("  Résultats du repository: " + results.size());
        return results;
    }

    // Rechercher les séances qui commencent exactement à une heure
    public List<Seance> getSeancesStartingAt(LocalDate date, LocalTime heure) {
        return seanceRepository.findSeancesStartingAt(date, heure);
    }

    // Rechercher les séances en cours à une heure donnée
    public List<Seance> getSeancesInProgress(LocalDate date, LocalTime heure) {
        return seanceRepository.findSeancesInProgress(date, heure);
    }

    //
    // Update
    public Seance updateSeance(Integer id, Seance seanceDetails) {
        Seance seance = seanceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Séance non trouvée avec l'id: " + id));

        if (seanceDetails.getFilm() != null && seanceDetails.getFilm().getId() != null) {
            Film film = filmRepository.findById(seanceDetails.getFilm().getId())
                    .orElseThrow(() -> new RuntimeException(
                            "Film non trouvé avec l'id: " + seanceDetails.getFilm().getId()));
            seance.setFilm(film);
        }

        if (seanceDetails.getSalle() != null && seanceDetails.getSalle().getId() != null) {
            Salle salle = salleRepository.findById(seanceDetails.getSalle().getId())
                    .orElseThrow(() -> new RuntimeException(
                            "Salle non trouvée avec l'id: " + seanceDetails.getSalle().getId()));
            seance.setSalle(salle);
        }

        seance.setDateSeance(seanceDetails.getDateSeance());
        seance.setHeureDebut(seanceDetails.getHeureDebut());
        seance.setHeureFin(seanceDetails.getHeureFin());

        return seanceRepository.save(seance);
    }

    // Delete
    public void deleteSeance(Integer id) {
        Seance seance = seanceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Séance non trouvée avec l'id: " + id));
        seanceRepository.delete(seance);
    }

    // Delete toutes les séances d'une salle

    /**
     * Calcule le nombre de places libres par type de siège pour une séance donnée
     * 
     * @param seanceId L'ID de la séance
     * @return Map avec le nom du type de siège comme clé et le nombre de places
     *         libres comme valeur
     */
    public Map<String, Integer> getAvailableSeatsByType(Integer seanceId) {
        // Récupérer la séance
        Seance seance = seanceRepository.findById(seanceId)
                .orElseThrow(() -> new RuntimeException("Séance non trouvée avec l'id: " + seanceId));

        // Récupérer la salle de la séance
        Salle salle = seance.getSalle();

        // Récupérer tous les sièges de la salle
        List<Siege> siegesDeLaSalle = siegeRepository.findBySalleId(salle.getId());

        // Compter le total de sièges par type
        Map<String, Integer> totalSeatsPerType = new HashMap<>();
        for (Siege siege : siegesDeLaSalle) {
            String typeName = siege.getTypeSiege().getNom();
            totalSeatsPerType.put(typeName, totalSeatsPerType.getOrDefault(typeName, 0) + 1);
        }

        List<Billet> billetsReserves = billetRepository.findBySeanceId(seanceId);

        Map<String, Integer> reservedSeatsPerType = new HashMap<>();
        for (Billet billet : billetsReserves) {
            String typeName = billet.getSiege().getTypeSiege().getNom();
            reservedSeatsPerType.put(typeName, reservedSeatsPerType.getOrDefault(typeName, 0) + 1);
        }

        Map<String, Integer> availableSeatsPerType = new HashMap<>();
        for (Map.Entry<String, Integer> entry : totalSeatsPerType.entrySet()) {
            String typeName = entry.getKey();
            int total = entry.getValue();
            int reserved = reservedSeatsPerType.getOrDefault(typeName, 0);
            availableSeatsPerType.put(typeName, total - reserved);
        }

        return availableSeatsPerType;
    }

    public int getRevenusSeance(Integer seanceId) {
        List<Billet> billets = billetRepository.findBySeanceId(seanceId);

        int totalRevenus = 0;

        for (Billet billet : billets) {
            if (billet.getPrix() != null) {
                totalRevenus += billet.getPrix();
            }
        }

        return totalRevenus;
    }
    
    public int getRevenusMaxSeance(Integer seanceId) {
        Seance seance = seanceRepository.findById(seanceId)
                .orElseThrow(() -> new RuntimeException("Séance non trouvée"));

        List<Siege> sieges = siegeRepository.findBySalleId(seance.getSalle().getId());

        int totalRevenusMax = 0;

        for (Siege siege : sieges) {
            if (siege.getTypeSiege() != null && siege.getTypeSiege().getPrix() != null) {
                totalRevenusMax += siege.getTypeSiege().getPrix();
            }
        }

        return totalRevenusMax;
    }

    public int getCapaciteMaxSeance(Integer seanceId) {
        Seance seance = seanceRepository.findById(seanceId)
                .orElseThrow(() -> new RuntimeException("Séance non trouvée"));

        List<Siege> sieges = siegeRepository.findBySalleId(seance.getSalle().getId());

        int totalCapacite = 0;

        for(Siege siege : sieges){
            totalCapacite += siege.getTypeSiege().getPrix();
        }

        // La capacité maximale est simplement le nombre total de sièges
        return totalCapacite;
    }

    /**
     * Récupère les types de sièges présents dans une salle
     */
    public List<TypeSiege> getTypesSiegesInSalle(Integer salleId) {
        List<Siege> sieges = siegeRepository.findBySalleId(salleId);
        Map<Integer, TypeSiege> typesSiegesMap = new HashMap<>();

        for (Siege siege : sieges) {
            TypeSiege type = siege.getTypeSiege();
            typesSiegesMap.put(type.getId(), type);
        }

        return new ArrayList<>(typesSiegesMap.values());
    }

    /**
     * Enregistre les tarifs pour une séance
     */


    /**
     * Vérifie si une séance chevauche d'autres séances dans la même salle
     */
    public boolean hasOverlap(Seance seance) {
        List<Seance> seancesSalle = seanceRepository.findBySalleIdAndDateSeance(
                seance.getSalle().getId(),
                seance.getDateSeance());

        for (Seance existante : seancesSalle) {
            // Vérifier si les horaires se chevauchent
            if (timesOverlap(seance.getHeureDebut(), seance.getHeureFin(),
                    existante.getHeureDebut(), existante.getHeureFin())) {
                return true;
            }
        }

        return false;
    }

    /**
     * Vérifie si une séance chevauche d'autres séances (en excluant une séance
     * spécifique)
     */
    public boolean hasOverlapExcluding(Seance seance, Integer excludeId) {
        List<Seance> seancesSalle = seanceRepository.findBySalleIdAndDateSeance(
                seance.getSalle().getId(),
                seance.getDateSeance());

        for (Seance existante : seancesSalle) {
            // Exclure la séance en cours de modification
            if (existante.getId().equals(excludeId)) {
                continue;
            }

            // Vérifier si les horaires se chevauchent
            if (timesOverlap(seance.getHeureDebut(), seance.getHeureFin(),
                    existante.getHeureDebut(), existante.getHeureFin())) {
                return true;
            }
        }

        return false;
    }

    /**
     * Vérifie si deux plages horaires se chevauchent
     */
    private boolean timesOverlap(LocalTime start1, LocalTime end1, LocalTime start2, LocalTime end2) {
        return start1.isBefore(end2) && start2.isBefore(end1);
    }

    /**
     * Met à jour les tarifs d'une séance
     * Note: Les prix sont maintenant définis au niveau du TypeSiege
     * Cette méthode n'est plus utilisée mais conservée pour compatibilité
     */
    @Deprecated
    public void updateTarifs(List<Integer> tarifIds, List<Integer> montants) {
        // Les prix sont maintenant gérés au niveau de TypeSiege
        // Cette méthode ne fait plus rien
        throw new UnsupportedOperationException("Les prix sont maintenant définis au niveau du TypeSiege. Utilisez TypeSiegeService pour modifier les prix.");
    }

    /**
     * Calcule le CA par séance avec les revenus des billets et des publicités
     */
    public Map<String, Object> calculateCAParSeance(Integer societeId, Integer seanceId, LocalDate dateDebut, LocalDate dateFin) {
        Map<String, Object> result = new HashMap<>();
        
        // Récupérer toutes les séances selon les filtres
        List<Seance> seances;
        if (seanceId != null) {
            seances = seanceRepository.findById(seanceId)
                    .map(Collections::singletonList)
                    .orElse(Collections.emptyList());
        } else if (dateDebut != null && dateFin != null) {
            seances = seanceRepository.findByDateSeanceBetween(dateDebut, dateFin);
        } else {
            seances = seanceRepository.findAll();
        }
        
        // Trier par date
        seances.sort(Comparator.comparing(Seance::getDateSeance)
                .thenComparing(Seance::getHeureDebut));
        
        List<Map<String, Object>> details = new ArrayList<>();
        double totalBillets = 0.0;
        double totalPublicites = 0.0;
        double totalCA = 0.0;
        double totalPaiements = 0.0;
        double totalResteAPayer = 0.0;
        
        for (Seance seance : seances) {
            // Calculer le revenu des billets vendus
            List<Billet> billets = billetRepository.findBySeanceId(seance.getId());
            double revenuBillets = billets.stream()
                    .mapToDouble(b -> b.getPrix() != null ? b.getPrix() : 0.0)
                    .sum();
            
            // Calculer le revenu des publicités pour cette séance
            List<Publicite> publicites = publiciteRepository.findBySeanceId(seance.getId());
            
            // Filtrer par société si spécifié
            if (societeId != null) {
                publicites = publicites.stream()
                        .filter(p -> p.getSociete() != null && p.getSociete().getId().equals(societeId))
                        .collect(java.util.stream.Collectors.toList());
            }
            
            double revenuPublicites = publicites.stream()
                    .mapToDouble(p -> {
                        if (p.getSociete() == null || p.getLongueur() == null || p.getNombre() == null) {
                            return 0.0;
                        }
                        double montantParMin = p.getSociete().getMontantParMin() != null ? p.getSociete().getMontantParMin() : 0.0;
                        double longueur = p.getLongueur();
                        int nombre = p.getNombre();
                        return montantParMin * longueur * nombre;
                    })
                    .sum();
            
            double caTotal = revenuBillets + revenuPublicites;
            
            // Calculer les paiements directs pour cette séance depuis paiement_detail
            double paiementsSeance = paiementDetailService.getDetailsBySeance(seance.getId()).stream()
                    .filter(d -> societeId == null || (d.getPaiement().getSociete() != null && 
                                 d.getPaiement().getSociete().getId().equals(societeId)))
                    .mapToDouble(d -> d.getMontant() != null ? d.getMontant() : 0.0)
                    .sum();
            
            double resteAPayerSeance = revenuPublicites - paiementsSeance;
            
            // Ajouter les détails de la séance
            Map<String, Object> detail = new HashMap<>();
            detail.put("seanceId", seance.getId());
            detail.put("film", seance.getFilm().getTitre());
            detail.put("dateSeance", seance.getDateSeance());
            detail.put("heureDebut", seance.getHeureDebut());
            detail.put("revenuBillets", revenuBillets);
            detail.put("revenuPublicites", revenuPublicites);
            detail.put("paiementsPublicites", paiementsSeance);
            detail.put("resteAPayerPublicites", resteAPayerSeance);
            detail.put("caTotal", caTotal);
            details.add(detail);
            
            totalBillets += revenuBillets;
            totalPublicites += revenuPublicites;
            totalPaiements += paiementsSeance;
            totalResteAPayer += resteAPayerSeance;
            totalCA += caTotal;
        }
        
        result.put("details", details);
        result.put("totalBillets", totalBillets);
        result.put("totalPublicites", totalPublicites);
        result.put("totalPaiements", totalPaiements);
        result.put("totalResteAPayer", totalResteAPayer);
        result.put("totalCA", totalCA);
        result.put("nombreSeances", seances.size());
        
        return result;
    }
}
