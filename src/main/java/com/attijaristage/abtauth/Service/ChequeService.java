package com.attijaristage.abtauth.Service;

import com.attijaristage.abtauth.Entities.Enum.ChequeStatus;
import com.attijaristage.abtauth.Entities.Cheque;
import com.attijaristage.abtauth.Repository.ChequeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class ChequeService {

    @Autowired
    private ChequeRepository chequeRepository;

    // Récupérer tous les chèques
    public List<Cheque> getAllCheques() {
        return chequeRepository.findAll();
    }

    // Créer un nouveau chèque
    public Cheque createCheque(String chequeNumber, BigDecimal amount, String beneficiary, Date dueDate) {
        Optional<Cheque> existingCheque = chequeRepository.findByChequeNumber(chequeNumber);
        if (existingCheque.isPresent()) {
            throw new IllegalStateException("Cheque with number " + chequeNumber + " already exists.");
        }

        Cheque cheque = new Cheque();
        cheque.setChequeNumber(chequeNumber);
        cheque.setAmount(amount);
        cheque.setBeneficiary(beneficiary);
        cheque.setIssueDate(new Date()); // Date actuelle
        cheque.setDueDate(dueDate);
        cheque.setStatus(ChequeStatus.EN_COURS);

        return chequeRepository.save(cheque);
    }

    // Accepter un chèque
    public Cheque encashCheque(String chequeNumber) {
        Cheque cheque = chequeRepository.findByChequeNumber(chequeNumber)
                .orElseThrow(() -> new IllegalStateException("Chèque non trouvé."));

        if (cheque.getStatus() == ChequeStatus.EN_COURS) {
            cheque.setStatus(ChequeStatus.ACCEPTE);
            return chequeRepository.save(cheque);
        }

        throw new IllegalStateException("Le chèque ne peut pas être encaissé.");
    }

    // Rejeter un chèque
    public Cheque rejectCheque(String chequeNumber) {
        Cheque cheque = chequeRepository.findByChequeNumber(chequeNumber)
                .orElseThrow(() -> new IllegalStateException("Chèque non trouvé."));

        if (cheque.getStatus() != ChequeStatus.ACCEPTE) {
            cheque.setStatus(ChequeStatus.REJETE);
            return chequeRepository.save(cheque);
        }

        throw new IllegalStateException("Le chèque ne peut pas être rejeté.");
    }

    // Obtenir les informations d'un chèque
    public Cheque getCheque(String chequeNumber) {
        return chequeRepository.findByChequeNumber(chequeNumber)
                .orElseThrow(() -> new IllegalStateException("Chèque non trouvé."));
    }

    // Supprimer un chèque
    public void deleteCheque(String chequeNumber) {
        Cheque cheque = chequeRepository.findByChequeNumber(chequeNumber)
                .orElseThrow(() -> new IllegalStateException("Chèque non trouvé."));

        chequeRepository.delete(cheque);
    }

    public Cheque updateChequeStatus(String chequeNumber, String newStatus) {
        Cheque cheque = chequeRepository.findByChequeNumber(chequeNumber)
                .orElseThrow(() -> new IllegalStateException("Chèque non trouvé."));
        cheque.setStatus(ChequeStatus.valueOf(newStatus)); // Assure-toi que la valeur est valide
        return chequeRepository.save(cheque);
    }

}
