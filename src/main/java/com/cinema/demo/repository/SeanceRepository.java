package com.cinema.demo.repository;

import com.cinema.demo.model.Seance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Repository
public interface SeanceRepository extends JpaRepository<Seance, Integer> {

    List<Seance> findByFilmId(Integer filmId);

    List<Seance> findBySalleId(Integer salleId);

    List<Seance> findByDateSeance(LocalDate dateSeance);

    List<Seance> findBySalleIdAndDateSeance(Integer salleId, LocalDate dateSeance);

    List<Seance> findByFilmIdAndDateSeance(Integer filmId, LocalDate dateSeance);

    // Rechercher les séances par nom de film (contient le texte)
    @Query("SELECT s FROM Seance s WHERE LOWER(s.film.titre) LIKE LOWER(CONCAT('%', :nomFilm, '%'))")
    List<Seance> findByFilmTitreContaining(@Param("nomFilm") String nomFilm);

    // Rechercher les séances en cours ou qui commencent à une heure donnée
    @Query("SELECT s FROM Seance s WHERE s.dateSeance = :date AND " +
            "s.heureDebut <= :heure AND s.heureFin > :heure")
    List<Seance> findSeancesAtTime(@Param("date") LocalDate date, @Param("heure") LocalTime heure);

    // Rechercher les séances par nom de film et qui sont en cours ou commencent à
    // une heure
    @Query("""
                SELECT s
                FROM Seance s
                JOIN s.film f
                WHERE LOWER(f.titre) LIKE LOWER(CONCAT('%', :nomFilm, '%'))
                  AND s.dateSeance = :date
                  AND s.heureDebut <= :heure
                  AND s.heureFin > :heure
            """)
    List<Seance> findByFilmAndDateAndTime(
            @Param("nomFilm") String nomFilm,
            @Param("date") LocalDate date,
            @Param("heure") LocalTime heure);

    // Rechercher les séances qui commencent exactement à une heure
    @Query("SELECT s FROM Seance s WHERE s.dateSeance = :date AND s.heureDebut = :heure")
    List<Seance> findSeancesStartingAt(@Param("date") LocalDate date, @Param("heure") LocalTime heure);

    // Rechercher les séances en cours à une heure donnée (entre debut et fin)
    @Query("SELECT s FROM Seance s WHERE s.dateSeance = :date AND s.heureDebut <= :heure AND s.heureFin > :heure")
    List<Seance> findSeancesInProgress(@Param("date") LocalDate date, @Param("heure") LocalTime heure);
    
    // Trouver les prochaines séances à partir d'une date
    List<Seance> findByDateSeanceGreaterThanEqualOrderByDateSeanceAscHeureDebutAsc(LocalDate date);
    
    // Trouver les séances entre deux dates
    List<Seance> findByDateSeanceBetween(LocalDate dateDebut, LocalDate dateFin);
}
