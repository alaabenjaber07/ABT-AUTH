package com.attijaristage.abtauth.Repository;

import com.attijaristage.abtauth.Entities.Virement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface VirementRepository extends JpaRepository<Virement, Long> {
    // Nombre total de virements
    @Query("SELECT COUNT(v) FROM Virement v")
    long getNombreVirementTotale();

    // Aujourd'hui
    @Query("SELECT COUNT(v) FROM Virement v WHERE v.dateVirement = :today")
    long countByDate(@Param("today") LocalDate today);

    // Ce mois-ci
    @Query("SELECT COUNT(v) FROM Virement v WHERE MONTH(v.dateVirement) = :month AND YEAR(v.dateVirement) = :year")
    long countByMonth(@Param("month") int month, @Param("year") int year);

    // Cette année
    @Query("SELECT COUNT(v) FROM Virement v WHERE YEAR(v.dateVirement) = :year")
    long countByYear(@Param("year") int year);

    // Historique : tous les virements ordonnés par date
    List<Virement> findAllByOrderByDateVirementDesc();

    // Nombre de virements par type d'instruction
    @Query("SELECT v.instructions, COUNT(v) FROM Virement v GROUP BY v.instructions")
    List<Object[]> countVirementsByInstruction();


}
