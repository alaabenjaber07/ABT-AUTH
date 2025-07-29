package com.attijaristage.abtauth.Service;

import com.attijaristage.abtauth.DTO.UserProfileDTO;
import com.attijaristage.abtauth.DTO.UserProfileMapper;
import com.attijaristage.abtauth.Entities.UserProfile;
import com.attijaristage.abtauth.Repository.UserProfileRepo;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
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

    public String loginUser(String username, String password) {
        return keycloakUserService.login(username, password);
    }


    // Méthode brute (optionnelle si non utilisée ailleurs)
    public List<UserRepresentation> getAllUsersRaw() {
        return keycloakUserService.getAllUsers();
    }

    // Méthode avec mapping vers DTO
    public List<UserProfileDTO> getAllUsers() {
        List<UserRepresentation> kcUsers = keycloakUserService.getAllUsers();

        return kcUsers.stream().map(kcUser -> {
            UserProfileDTO dto = new UserProfileDTO();
            dto.setUsername(kcUser.getUsername());
            dto.setEmail(kcUser.getEmail());
            dto.setFirstName(kcUser.getFirstName());
            dto.setLastName(kcUser.getLastName());

            Optional<UserProfile> optProfile = userProfileRepository.findByKeycloakId(kcUser.getId());
            if (optProfile.isPresent()) {
                UserProfile profile = optProfile.get();
                dto.setIdUserprofile(profile.getIdUserprofile());  // Ajout ID ici
                dto.setMatricule(profile.getMatricule());
                dto.setAddress(profile.getAddress());
                dto.setPhoneNumber(profile.getPhoneNumber());
                dto.setKeycloakId(profile.getKeycloakId());
            } else {
                dto.setIdUserprofile(null);
                dto.setMatricule(null);
                dto.setAddress(null);
                dto.setPhoneNumber(null);
                dto.setKeycloakId(kcUser.getId());
            }

            return dto;
        }).collect(Collectors.toList());
    }



    public void deleteUserProfile(Long idUserprofile) {
        Optional<UserProfile> optional = userProfileRepository.findById(idUserprofile);
        if (optional.isPresent()) {
            UserProfile profile = optional.get();
            try {
                keycloakUserService.deleteUserById(profile.getKeycloakId());
                userProfileRepository.deleteById(idUserprofile);
            } catch (Exception e) {
                // Log l’erreur
                System.err.println("Erreur lors de la suppression dans Keycloak ou base : " + e.getMessage());
                throw new RuntimeException("Erreur lors de la suppression de l'utilisateur");
            }
        } else {
            throw new RuntimeException("Utilisateur non trouvé");
        }


    }


    public UserProfileDTO getUserProfileById(Long idUserprofile) {
        Optional<UserProfile> optional = userProfileRepository.findById(idUserprofile);
        if (optional.isEmpty()) {
            throw new NoSuchElementException("Utilisateur non trouvé avec id : " + idUserprofile);
        }
        UserProfile profile = optional.get();

        // Récupérer l’utilisateur Keycloak via keycloakId
        UserRepresentation kcUser = keycloakUserService.getUserById(profile.getKeycloakId());
        if (kcUser == null) {
            throw new RuntimeException("Utilisateur Keycloak non trouvé avec id : " + profile.getKeycloakId());
        }

        // Créer le DTO complet
        UserProfileDTO dto = new UserProfileDTO();
        dto.setIdUserprofile(profile.getIdUserprofile());
        dto.setKeycloakId(profile.getKeycloakId());
        dto.setMatricule(profile.getMatricule());
        dto.setAddress(profile.getAddress());
        dto.setPhoneNumber(profile.getPhoneNumber());

        // Remplir les données Keycloak
        dto.setUsername(kcUser.getUsername());
        dto.setEmail(kcUser.getEmail());
        dto.setFirstName(kcUser.getFirstName());
        dto.setLastName(kcUser.getLastName());

        return dto;
    }
    public UserProfile getByKeycloakId(String keycloakId) {
        return userProfileRepository.findByKeycloakId(keycloakId)
                .orElseThrow(() -> new RuntimeException("Utilisateur introuvable avec le keycloakId : " + keycloakId));
    }




}
