package com.attijaristage.abtauth.DTO;

import com.attijaristage.abtauth.Entities.UserProfile;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.security.access.prepost.PreAuthorize;


public class UserProfileMapper {
    public static UserProfileDTO toDTO(UserProfile entity, Keycloak keycloak, String realm) {
        if (entity == null) return null;

        UserProfileDTO dto = new UserProfileDTO();

        // Données stockées en base
        dto.setKeycloakId(entity.getKeycloakId());
        dto.setMatricule(entity.getMatricule());
        dto.setAddress(entity.getAddress());
        dto.setPhoneNumber(entity.getPhoneNumber());
        dto.setDateOfBirth(entity.getDateOfBirth());

        // Données venant de Keycloak
        try {
            UserRepresentation keycloakUser = keycloak.realm(realm).users().get(entity.getKeycloakId()).toRepresentation();
            dto.setUsername(keycloakUser.getUsername());
            dto.setEmail(keycloakUser.getEmail());
            dto.setFirstName(keycloakUser.getFirstName());
            dto.setLastName(keycloakUser.getLastName());
        } catch (Exception e) {
            System.err.println("Erreur lors de la récupération des infos Keycloak pour l'utilisateur avec ID: " + entity.getKeycloakId());
            e.printStackTrace();
        }

        return dto;
    }

    public static UserProfile toEntity(UserProfileDTO dto) {
        if (dto == null) return null;
        UserProfile entity = new UserProfile();
        entity.setIdUserProfile(dto.getIdUserprofile());  // set ID (utile pour update)
        entity.setKeycloakId(dto.getKeycloakId());
        entity.setMatricule(dto.getMatricule());
        entity.setAddress(dto.getAddress());
        entity.setPhoneNumber(dto.getPhoneNumber());
        entity.setDateOfBirth(dto.getDateOfBirth());
        return entity;
    }
}
