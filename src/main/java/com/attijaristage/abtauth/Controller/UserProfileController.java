package com.attijaristage.abtauth.Controller;

import com.attijaristage.abtauth.DTO.LoginDTO;
import com.attijaristage.abtauth.DTO.UserProfileDTO;
import com.attijaristage.abtauth.Entities.UserProfile;
import com.attijaristage.abtauth.Service.KeycloakUserService;
import com.attijaristage.abtauth.Service.UserProfileService;
import org.springframework.security.access.prepost.PreAuthorize;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/user-profiles")
@CrossOrigin(origins = "http://localhost:4200")
public class UserProfileController {

    @Autowired
    private UserProfileService userProfileService;
    @Autowired
    private KeycloakUserService keycloakUserService;
    private UserProfileDTO dto;

    @PostMapping("/register")
    @PreAuthorize("hasRole('admin')")
    public ResponseEntity<?> register(@RequestBody UserProfileDTO dto) {
        try {
            userProfileService.registerUser(dto);
            return ResponseEntity.ok("Utilisateur créé avec succès !");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erreur : " + e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDTO loginDTO) {
        try {
            String token = userProfileService.loginUser(loginDTO.getUsername(), loginDTO.getPassword());
            return ResponseEntity.ok(token);
        } catch (Exception e) {
            return ResponseEntity.status(401).body("Échec de l'authentification : " + e.getMessage());
        }
    }

    @GetMapping
    @PreAuthorize("hasRole('admin')")
    public ResponseEntity<List<UserProfileDTO>> getAllUsers() {
        List<UserProfileDTO> users = userProfileService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @DeleteMapping("/id")
    @PreAuthorize("hasRole('admin')")
    public ResponseEntity<Object> delete(@PathVariable Long id) {
        System.out.println("Suppression demandée pour l'ID: " + id);
        return userProfileService.getById(id).map(existing -> {
            keycloakUserService.deleteUserInKeycloak(existing.getKeycloakId());
            userProfileService.delete(id);
            return ResponseEntity.noContent().build();
        }).orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/id")
    @PreAuthorize("hasRole('admin')")
    public ResponseEntity<?> getUserProfile(@RequestParam Long id) {
        try {
            UserProfileDTO dto = userProfileService.getUserProfileById(id);
            return ResponseEntity.ok(dto);
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Utilisateur introuvable.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erreur interne : " + e.getMessage());
        }
    }

    @PatchMapping
    @PreAuthorize("hasRole('admin')")
    public ResponseEntity<UserProfile> update(@RequestBody UserProfileDTO dto) {
        if (dto.getIdUserprofile() == null) {
            return ResponseEntity.badRequest().body(null);
        }
        return userProfileService.update(dto.getIdUserprofile(), dto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    @GetMapping("/role/id")
    @PreAuthorize("hasRole('admin')")
    public ResponseEntity<String> getUserRole(@RequestParam Long id) {
        String keycloakId=userProfileService.getKeycloakIdById(id);
        String role = keycloakUserService.getUserRoleByKeycloakId(keycloakId);
        return ResponseEntity.ok(role);
    }

    @PutMapping("/role")
    @PreAuthorize("hasRole('admin')")
    public ResponseEntity<String> assignRoleToUser(@RequestBody Map<String, String> payload) {
        Long userId = Long.parseLong(payload.get("id"));
        String role = payload.get("role");
        String keycloakId=userProfileService.getKeycloakIdById(userId);
        keycloakUserService.setUserRole(keycloakId, role);
        return ResponseEntity.ok("Rôle '" + role + "' assigné avec succès à l'utilisateur.");
    }



}
