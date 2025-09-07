package com.attijaristage.abtauth.Controller;

import com.attijaristage.abtauth.DTO.EffetsDTO;
import com.attijaristage.abtauth.DTO.EffetsMapper;
import com.attijaristage.abtauth.Entities.Effets;
import com.attijaristage.abtauth.Service.EffetService;
import jakarta.persistence.EntityNotFoundException;
import org.keycloak.KeycloakPrincipal;
import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/effets")
@CrossOrigin(origins = "http://localhost:4200")
public class EffetController {
    @Autowired
    private EffetService effetService;

    @PostMapping("/create")
    public ResponseEntity<?> createEffet(@RequestBody EffetsDTO dto,Authentication authentication){
        try{
            String keycloakId = authentication.getName();
            effetService.create(dto,keycloakId);
            return ResponseEntity.ok("Effet créé avec succès !");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erreur : " + e.getMessage());
        }
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id){
        try {
            effetService.delete(id);
            return ResponseEntity.ok("Effet supprimé avec succès ! ");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erreur: "+e.getMessage());
        }
    }
    @GetMapping
    public ResponseEntity<?> getEffets() {
        List<EffetsDTO> effets = effetService.getAll();
        try{
            if(effets.isEmpty()){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Effets introuvables");
            }else{
                return ResponseEntity.ok(effets);
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erreur lors de la récupération de l'effet");
        }

    }
    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        try {
            Optional<Effets> effet = effetService.getById(id);
            if (effet.isPresent()) {
                return ResponseEntity.ok(EffetsMapper.toDTO(effet.get()));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Effet introuvable");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erreur lors de la récupération de l'effet");
        }
    }
    @PostMapping("/delete-multiple")
    public ResponseEntity<?> deleteMultiple(@RequestBody List<Long> ids){
        try {
            effetService.deleteMultiple(ids);
            return ResponseEntity.ok("Effets supprimés avec succès ! ");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erreur: "+e.getMessage());
        }
    }
    @PutMapping("/{id}/etat")
    public ResponseEntity<?> updateEtat(@PathVariable Long id, @RequestBody Map<String, String> payload) {
        String nouvelEtat = payload.get("etat");
        effetService.updateEtat(id, nouvelEtat);
        return ResponseEntity.ok().build();
    }
    @PostMapping("/import")
    public ResponseEntity<?> importXml(@RequestBody String xml) {
        try {
            List<Effets> savedEffets = effetService.convertToObjectsAndSave(xml);
            return ResponseEntity.ok(savedEffets);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erreur: " + e.getMessage());
        }
    }


    @GetMapping("/{id}/export")
    public ResponseEntity<String> exportXmlById(@PathVariable Long id) throws Exception {
        Effets effet = effetService.getById(id)
                .orElseThrow(() -> new EntityNotFoundException("Effet avec ID " + id + " introuvable."));
        EffetsDTO dto = EffetsMapper.toDTO(effet);

        String xml = effetService.convertToXml(List.of(dto));
        return ResponseEntity.ok(xml);
    }






}
