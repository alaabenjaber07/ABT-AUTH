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

    private final RestTemplate restTemplate = new RestTemplate();

    public void changePassword(String token, String realm, ChangePasswordRequest request) {
        String url = "http://localhost:8080/realms/" + realm + "/account/credentials/password";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(token); // Authorization: Bearer token

        HttpEntity<ChangePasswordRequest> entity = new HttpEntity<>(request, headers);

        try {
            restTemplate.exchange(url, HttpMethod.PUT, entity, Void.class);
        } catch (Exception e) {
            throw new RuntimeException("Erreur changement mot de passe: " + e.getMessage(), e);
        }
    }
}

