package com.attijaristage.abtauth.Controller;

import com.attijaristage.abtauth.Entities.Cheque;
import com.attijaristage.abtauth.Entities.UserProfile;
import com.attijaristage.abtauth.Repository.UserProfileRepo;
import com.attijaristage.abtauth.Service.ChequeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/cheques")
public class ChequeController {

    @Autowired
    private ChequeService chequeService;
    @Autowired
    private UserProfileRepo userProfileRepo;
    @PostMapping("/create")
    public ResponseEntity<Object> createCheque(@RequestBody Cheque cheque, Principal principal) {
        try {
            // Récupérer le user depuis le principal (JWT Keycloak)
            String keycloakId = principal.getName();  // ou utiliser SecurityContextHolder
            UserProfile user = userProfileRepo.findByKeycloakId(keycloakId)
                    .orElseThrow(() -> new RuntimeException("Utilisateur introuvable"));

            // Créer le chèque avec l'utilisateur récupéré côté serveur
            Cheque createdCheque = chequeService.createCheque(
                    cheque.getChequeNumber(),
                    cheque.getAmount(),
                    cheque.getBeneficiary(),
                    cheque.getDueDate(),
                    user
            );

            return ResponseEntity.ok(createdCheque);
        } catch (IllegalStateException e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }



    @GetMapping
    public List<Cheque> getAllCheques() {
        return chequeService.getAllCheques();
    }


    // Accepter un chèque
    @PostMapping("/encash/{chequeNumber}")
    public Cheque encashCheque(@PathVariable String chequeNumber) {
        return chequeService.encashCheque(chequeNumber);
    }

    // Rejeter un chèque
    @PostMapping("/reject/{chequeNumber}")
    public Cheque rejectCheque(@PathVariable String chequeNumber) {
        return chequeService.rejectCheque(chequeNumber);
    }

    // Obtenir les informations d'un chèque
    @GetMapping("/{chequeNumber}")
    public Cheque getCheque(@PathVariable String chequeNumber) {
        return chequeService.getCheque(chequeNumber);
    }

    // Endpoint pour supprimer un chèque par son numéro
    @DeleteMapping("/delete/{chequeNumber}")
    public ResponseEntity<String> deleteCheque(@PathVariable String chequeNumber) {
        try {
            chequeService.deleteCheque(chequeNumber);
            return ResponseEntity.ok("Cheque deleted successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(404).body("Cheque not found.");
        }
    }

    @PutMapping("/update-status/{chequeNumber}")
    public ResponseEntity<Cheque> updateStatus(@PathVariable String chequeNumber, @RequestBody Map<String, String> body) {
        String newStatus = body.get("status");
        Cheque updatedCheque = chequeService.updateChequeStatus(chequeNumber, newStatus);
        return ResponseEntity.ok(updatedCheque);
    }

}
