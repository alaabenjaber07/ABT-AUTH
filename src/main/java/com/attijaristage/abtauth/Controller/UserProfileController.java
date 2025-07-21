package com.attijaristage.abtauth.Controller;

import com.attijaristage.abtauth.DTO.UserProfileDTO;
import com.attijaristage.abtauth.Entities.UserProfile;
import com.attijaristage.abtauth.Service.KeycloakUserService;
import com.attijaristage.abtauth.Service.UserProfileService;
import org.springframework.security.access.prepost.PreAuthorize;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController

@RequestMapping("/api/user-profiles")
public class UserProfileController {
    @Autowired
    private  UserProfileService service;
    @Autowired
    private KeycloakUserService keycloakUserService;
    private UserProfileDTO dto;

    @PostMapping
    @PreAuthorize("hasRole('admin')")
    public ResponseEntity<UserProfile> create(@Valid @RequestBody UserProfileDTO dto) {
        String keycloakId = keycloakUserService.createUserInKeycloak(dto);
        UserProfile created = service.create(dto,keycloakId);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }
    @GetMapping
    @PreAuthorize("hasRole('admin')")
    public List<UserProfileDTO> getAll() {
        return service.getAll();
    }

    @PostMapping("/get")
    @PreAuthorize("hasRole('admin')")
    public ResponseEntity<UserProfileDTO> getById(@RequestBody Map<String, Long> payload)
    {   Long id = payload.get("id");
        return service.getById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping
    @PreAuthorize("hasRole('admin')")
    public ResponseEntity<UserProfile> update(@RequestBody UserProfileDTO dto) {
        if (dto.getIdUserprofile() == null) {
            return ResponseEntity.badRequest().body(null);
        }
        return service.update(dto.getIdUserprofile(), dto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    @DeleteMapping
    @PreAuthorize("hasRole('admin')")
    public ResponseEntity<Object> delete(@RequestBody Map<String, Long> payload) {
        Long id = payload.get("id");
        return service.getById(id).map(existing -> {
            keycloakUserService.deleteUserInKeycloak(existing.getKeycloakId());
            service.delete(id);
            return ResponseEntity.noContent().build();
        }).orElse(ResponseEntity.notFound().build());
    }
    @PostMapping("/role")
    @PreAuthorize("hasRole('admin')")
    public ResponseEntity<String> getUserRole(@RequestBody Map<String, Long> payload) {
        Long userId = payload.get("id");
        String keycloakId=service.getKeycloakIdById(userId);
        String role = keycloakUserService.getUserRoleByKeycloakId(keycloakId);
        return ResponseEntity.ok(role);
    }

    @PutMapping("/role")
    @PreAuthorize("hasRole('admin')")
    public ResponseEntity<String> assignRoleToUser(@RequestBody Map<String, String> payload) {
        Long userId = Long.parseLong(payload.get("id"));
        String role = payload.get("role");
        String keycloakId=service.getKeycloakIdById(userId);
        keycloakUserService.setUserRole(keycloakId, role);
        return ResponseEntity.ok("Rôle '" + role + "' assigné avec succès à l'utilisateur.");
    }

}
