package com.attijaristage.abtauth.Repository;

import com.attijaristage.abtauth.Entities.Effets;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EffetsRepo extends JpaRepository<Effets, Long> {

    @Query("SELECT e.typeEffet, COUNT(e) FROM Effets e GROUP BY e.typeEffet")
    List<Object[]> countEffetsByType();
    @Query(value = "SELECT * FROM (" +
            "  SELECT date_ajout, COUNT(*) AS count " +
            "  FROM effets " +
            "  GROUP BY date_ajout " +
            "  ORDER BY date_ajout DESC )" , nativeQuery = true)
    List<Object[]> countEffetsByDate();

    @Query("SELECT e.user.keycloakId, COUNT(e) FROM Effets e GROUP BY e.user.keycloakId")
    List<Object[]> countEffetsByUser();
    @Query("SELECT e.etat, COUNT(e) FROM Effets e GROUP BY e.etat")  // <-- corrigé ici
    List<Object[]> countEffetsByEtat();
}

