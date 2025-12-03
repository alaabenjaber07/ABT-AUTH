package com.attijaristage.abtauth.Repository;

import com.attijaristage.abtauth.Entities.Cheque;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface ChequeRepository extends JpaRepository<Cheque, Long> {
    Optional<Cheque> findByChequeNumber(String chequeNumber);

    @Query("SELECT c.status,COUNT(c) FROM Cheque c GROUP BY c.status")
    List<Object[]> countChequesBystatus();
    @Query("SELECT c.user.keycloakId , COUNT(c) FROM Cheque c GROUP BY c.user.keycloakId")
    List<Object[]> countChequesByUser();

}
