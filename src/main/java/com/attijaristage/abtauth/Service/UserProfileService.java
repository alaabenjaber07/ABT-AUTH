package com.attijaristage.abtauth.Service;

import com.attijaristage.abtauth.DTO.UserProfileDTO;
import com.attijaristage.abtauth.DTO.UserProfileMapper;
import com.attijaristage.abtauth.Entities.UserProfile;
import com.attijaristage.abtauth.Repository.UserProfileRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserProfileService {
    @Autowired
    private KeycloakUserService keycloakUserService;

    @Autowired
    private UserProfileRepo userProfileRepository;

    public void registerUser(UserProfileDTO dto) {
        // 1. Créer dans Keycloak
        String keycloakId = keycloakUserService.createUser(
                dto.getUsername(),
                dto.getEmail(),
                dto.getFirstName(),
                dto.getLastName(),
                dto.getPassword()
        );

        // 2. Sauvegarder dans la base
        UserProfile profile = new UserProfile();
        profile.setKeycloakId(keycloakId);
        profile.setMatricule(dto.getMatricule());
        profile.setAddress(dto.getAddress());
        profile.setPhoneNumber(dto.getPhoneNumber());

        userProfileRepository.save(profile);
    }
}
