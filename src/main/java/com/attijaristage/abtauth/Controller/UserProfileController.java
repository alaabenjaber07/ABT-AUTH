package com.attijaristage.abtauth.Controller;

import com.attijaristage.abtauth.DTO.UserProfileDTO;
import com.attijaristage.abtauth.Service.UserProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;


@RestController
@RequestMapping("/api/user-profiles")
@CrossOrigin("*")
public class UserProfileController {

    @Autowired
    private UserProfileService userProfileService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody UserProfileDTO dto) {
        try {
            userProfileService.registerUser(dto);
            return ResponseEntity.ok("Utilisateur créé avec succès !");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erreur : " + e.getMessage());
        }
    }
}
