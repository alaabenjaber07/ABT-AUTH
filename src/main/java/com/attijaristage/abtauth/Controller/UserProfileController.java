package com.attijaristage.abtauth.Controller;

import com.attijaristage.abtauth.DTO.UserProfileDTO;
import com.attijaristage.abtauth.Entities.UserProfile;
import com.attijaristage.abtauth.Service.KeycloakUserService;
import com.attijaristage.abtauth.Service.UserProfileService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user-profiles")
public class UserProfileController {
    @Autowired
    private  UserProfileService service;
    @Autowired
    private KeycloakUserService keycloakUserService;
    private UserProfileDTO dto;


    @PostMapping
    public ResponseEntity<UserProfile> create(@Valid @RequestBody UserProfileDTO dto) {
        String keycloakId = keycloakUserService.createUserInKeycloak(dto);
        UserProfile created = service.create(dto,keycloakId);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }
    @GetMapping
    public List<UserProfileDTO> getAll() {
        return service.getAll();
    }
    @GetMapping("/{id}")
    public ResponseEntity<UserProfile> getById(@PathVariable Long id) {
        return service.getById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    @PutMapping("/{id}")
    public ResponseEntity<UserProfile> update(@PathVariable Long id, @RequestBody UserProfileDTO dto) {
        return service.update(id, dto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> delete(@PathVariable Long id) {
        return service.getById(id).map(existing -> {
            keycloakUserService.deleteUserInKeycloak(existing.getKeycloakId());
            service.delete(id);
            return ResponseEntity.noContent().build();
        }).orElse(ResponseEntity.notFound().build());
    }

}
