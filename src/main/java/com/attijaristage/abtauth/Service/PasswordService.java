package com.attijaristage.abtauth.Service;

import com.attijaristage.abtauth.DTO.ChangePasswordRequest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class PasswordService {

    private final KeycloakUserService keycloakUserService;

    public PasswordService(KeycloakUserService keycloakUserService) {
        this.keycloakUserService = keycloakUserService;
    }

    public void changePassword(String username, ChangePasswordRequest request) {
        // Vérification que le nouveau mot de passe correspond à la confirmation
        if (!request.getNewPassword().equals(request.getConfirmPassword())) {
            throw new IllegalArgumentException("Le nouveau mot de passe et sa confirmation ne correspondent pas !");
        }

        // Vérification de l'ancien mot de passe
        try {
            keycloakUserService.login(username, request.getCurrentPassword());
        } catch (Exception e) {
            throw new IllegalArgumentException("error "+e.getMessage());
        }

        keycloakUserService.changeUserPassword(request.getKeycloakId(), request.getNewPassword());
    }
}
