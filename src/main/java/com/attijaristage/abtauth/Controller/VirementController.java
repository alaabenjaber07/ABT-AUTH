package com.attijaristage.abtauth.Controller;

import com.attijaristage.abtauth.DTO.VirementDTO;
import com.attijaristage.abtauth.Entities.Virement;
import com.attijaristage.abtauth.Repository.VirementRepository;
import com.attijaristage.abtauth.Service.Validation.XmlValidationService;
import com.attijaristage.abtauth.Service.VirementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/virements")
public class VirementController {
    @Autowired
    private VirementService virementService;

    @Autowired
    private VirementRepository virementRepository;


    @Autowired
    private XmlValidationService xmlValidationService;
    @PostMapping("/create")
    public ResponseEntity<String> addVirement(@RequestBody VirementDTO virementDTO) {
        try {
            // 1) Validation XML
            xmlValidationService.marshalAndValidate(virementDTO);

            // 2) si exacte dans BD sinon efface
            virementService.ajouterVirement(virementDTO);
            return ResponseEntity.ok("✅ Virement ajouté avec succès");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("❌ Erreur (validation XML): " + e.getMessage());
        }
    }
    @PostMapping("/validate-xml")
    public ResponseEntity<?> validateXml(@RequestBody VirementDTO virementDTO) {
        try {
            String xml = xmlValidationService.marshalAndValidate(virementDTO);
            return ResponseEntity.ok(xml); // verfie si xml
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("❌ Invalid XML: " + e.getMessage());
        }
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAllVirements() {
        try {
            List<VirementDTO> virementsDTO = virementService.getAllVirementsDTO();
            return ResponseEntity.ok(virementsDTO);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erreur lors de la récupération des virements : " + e.getMessage());
        }
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteVirement(@PathVariable Long id) {
        try {
            virementService.deleteVirement(id);
            return ResponseEntity.ok("Virement supprimé avec succès.");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateVirement(@PathVariable Long id, @RequestBody VirementDTO virementDTO) {
        try {
            VirementDTO updated = virementService.updateVirement(id, virementDTO);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Erreur : " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erreur serveur");
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Virement> getVirementById(@PathVariable Long id) {
        Optional<Virement> virement = virementRepository.findById(id);
        return virement.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // 1) Nombre total de virements
    @GetMapping("/nombre-total")
    public ResponseEntity<Long> getNombreVirementTotale() {
        return ResponseEntity.ok(virementService.getNombreVirementTotale());
    }

    // 2) Nombre de virements par jour
    @GetMapping("/dashboard")
    public ResponseEntity<Map<String, Long>> getDashboard() {
        Map<String, Long> stats = new HashMap<>();
        stats.put("today", virementService.getNombreToday());
        stats.put("month", virementService.getNombreThisMonth());
        stats.put("year", virementService.getNombreThisYear());
        return ResponseEntity.ok(stats);
    }


    // 3) Historique des virements
    @GetMapping("/historique")
    public ResponseEntity<List<VirementDTO>> getHistoriqueVirement() {
        return ResponseEntity.ok(virementService.getHistoriqueVirement());
    }

    // 4) Nombre de virements par instruction
    @GetMapping("/nombre-par-instruction")
    public ResponseEntity<Map<String, Long>> getNombreVirementsParInstruction() {
        return ResponseEntity.ok(virementService.getNombreVirementsParInstruction());
    }



}