package com.attijaristage.abtauth.Service;

import com.attijaristage.abtauth.DTO.UserProfileDTO;
import com.attijaristage.abtauth.DTO.UserProfileMapper;
import com.attijaristage.abtauth.Entities.UserProfile;
import com.attijaristage.abtauth.Repository.UserProfileRepo;
import org.keycloak.admin.client.Keycloak;
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
    private String realm="carthago-realm";
    public void registerUser(UserProfileDTO dto) {
        // 1. Créer dans Keycloak
        String keycloakId = keycloakUserService.createUser(
                dto.getUsername(),
                dto.getEmail(),
                dto.getFirstName(),
                dto.getLastName(),
                dto.getPassword()
        );
        keycloakUserService.setUserRole(keycloakId, dto.getRole());
        UserProfile profile = new UserProfile();
        profile.setKeycloakId(keycloakId);
        profile.setMatricule(dto.getMatricule());
        profile.setAddress(dto.getAddress());
        profile.setPhoneNumber(dto.getPhoneNumber());
        profile.setDateOfBirth(dto.getDateOfBirth());

        userProfileRepository.save(profile);
    }


    public String loginUser(String username, String password) {
        return keycloakUserService.login(username, password);
    }
    public Optional<UserProfileDTO> getById(Long id) {
        return userProfileRepository.findById(id).map(user -> {
            Keycloak keycloak = keycloakUserService.getKeycloak();
            return UserProfileMapper.toDTO(user, keycloak, realm);
        });
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
                dto.setIdUserprofile(profile.getIdUserProfile());  // Ajout ID ici
                dto.setMatricule(profile.getMatricule());
                dto.setAddress(profile.getAddress());
                dto.setPhoneNumber(profile.getPhoneNumber());
                dto.setKeycloakId(profile.getKeycloakId());
                dto.setDateOfBirth(profile.getDateOfBirth());
                dto.setRole(keycloakUserService.getUserRoleByKeycloakId(profile.getKeycloakId()));
            } else {
                dto.setIdUserprofile(null);
                dto.setMatricule(null);
                dto.setAddress(null);
                dto.setPhoneNumber(null);
                dto.setDateOfBirth(null);
                dto.setKeycloakId(kcUser.getId());

            }

            return dto;
        }).collect(Collectors.toList());
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
        dto.setIdUserprofile(profile.getIdUserProfile());
        dto.setKeycloakId(profile.getKeycloakId());
        dto.setMatricule(profile.getMatricule());
        dto.setAddress(profile.getAddress());
        dto.setPhoneNumber(profile.getPhoneNumber());
        dto.setDateOfBirth(profile.getDateOfBirth());

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




    public void delete(Long id) {
        if (userProfileRepository.existsById(id)) {
            userProfileRepository.deleteById(id);
        }
    }
    /*UPDATE*/

    public Optional<UserProfile> update(Long id, UserProfileDTO dto) {
        return userProfileRepository.findById(id).map(existingUser -> {
            if (existingUser.getKeycloakId() != null) {
                keycloakUserService.updateUserInKeycloak(dto, existingUser.getKeycloakId());
            }
            if (dto.getDateOfBirth() != null) {
                existingUser.setDateOfBirth(dto.getDateOfBirth());
            }
            if (dto.getPhoneNumber() != null) {
                existingUser.setPhoneNumber(dto.getPhoneNumber());
            }
            if (dto.getKeycloakId() != null) {
                existingUser.setKeycloakId(dto.getKeycloakId());

            }
            if (dto.getAddress() != null) {
                existingUser.setAddress(dto.getAddress());

            }
            if (dto.getMatricule() != null) {
                existingUser.setMatricule(dto.getMatricule());

            }

            return userProfileRepository.save(existingUser);
        });
    }

    public String getKeycloakIdById(Long idUserprofile) {
        return userProfileRepository.findById(idUserprofile)
                .map(userProfile -> userProfile.getKeycloakId())
                .orElseThrow(() -> new RuntimeException("Utilisateur introuvable pour id : " + idUserprofile));
    }

}
