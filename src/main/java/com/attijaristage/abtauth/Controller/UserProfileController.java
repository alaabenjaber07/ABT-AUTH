package com.attijaristage.abtauth.Controller;

import com.attijaristage.abtauth.DTO.LoginDTO;
import com.attijaristage.abtauth.DTO.UserProfileDTO;
import com.attijaristage.abtauth.Service.UserProfileService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/user-profiles")
@CrossOrigin(origins = "http://localhost:4200")
public class UserProfileController {

    @Autowired
    private UserProfileService userProfileService;

    //  Enregistrer un utilisateur
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody UserProfileDTO dto) {
        try {
            userProfileService.registerUser(dto);
            return ResponseEntity.ok("Utilisateur créé avec succès !");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erreur : " + e.getMessage());
        }
    }

    // Login
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDTO loginDTO) {
        try {
            String token = userProfileService.loginUser(loginDTO.getUsername(), loginDTO.getPassword());
            return ResponseEntity.ok(token);
        } catch (Exception e) {
            return ResponseEntity.status(401).body("Échec de l'authentification : " + e.getMessage());
        }
    }

    // Récupérer tous les utilisateurs
    @GetMapping
    public ResponseEntity<List<UserProfileDTO>> getAllUsers() {
        List<UserProfileDTO> users = userProfileService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    //  Supprimer un utilisateur
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        try {
            userProfileService.deleteUserProfile(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erreur lors de la suppression : " + e.getMessage());
        }
    }

    //  Récupérer un utilisateur par ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getUserProfile(@PathVariable Long id) {
        try {
            UserProfileDTO dto = userProfileService.getUserProfileById(id);
            return ResponseEntity.ok(dto);
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Utilisateur introuvable.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erreur interne : " + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateUserProfile(@PathVariable Long id, @RequestBody UserProfileDTO dto) {
        try {
            userProfileService.updateUserProfile(id, dto);
            return ResponseEntity.ok("Utilisateur mis à jour avec succès.");
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Utilisateur non trouvé.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erreur lors de la mise à jour : " + e.getMessage());
        }
    }



}
