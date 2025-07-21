package com.attijaristage.abtauth.Service;

import com.attijaristage.abtauth.DTO.UserProfileDTO;
import com.attijaristage.abtauth.DTO.UserProfileMapper;
import com.attijaristage.abtauth.Entities.UserProfile;
import com.attijaristage.abtauth.Repository.UserProfileRepo;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;

import org.keycloak.admin.client.Keycloak;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserProfileService {
    @Autowired
    private  UserProfileRepo repo ;
    @Autowired
    private KeycloakUserService keycloakUserService;
    private String realm="carthago-realm";


    public UserProfile create(UserProfileDTO dto,String keycloakId){
        UserProfile user = new UserProfile();
        user.setKeycloakId(keycloakId);
        user.setDateOfBirth(dto.getDateOfBirth());
        user.setPhoneNumber(dto.getPhoneNumber());
        user.setAddress(dto.getAddress());
        user.setMatricule(dto.getMatricule());
        return repo.save(user);
    }
    public List<UserProfileDTO> getAll() {
        Keycloak keycloak = keycloakUserService.getKeycloak();
        return repo.findAll()
                .stream()
                .map(user -> UserProfileMapper.toDTO(user, keycloak, realm))
                .collect(Collectors.toList());
    }

    public Optional<UserProfileDTO> getById(Long id) {
        return repo.findById(id).map(user -> {
            Keycloak keycloak = keycloakUserService.getKeycloak();
            return UserProfileMapper.toDTO(user, keycloak, realm);
        });
    }
    public void delete(Long id){
        if (repo.existsById(id)){
            repo.deleteById(id);
        }
    }
    /*UPDATE*/
    public Optional<UserProfile> update(Long id, UserProfileDTO dto) {
        return repo.findById(id).map(existingUser -> {
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

            return repo.save(existingUser);
        });
    }
    public String getKeycloakIdById(Long idUserprofile) {
        return repo.findById(idUserprofile)
                .map(userProfile -> userProfile.getKeycloakId())
                .orElseThrow(() -> new RuntimeException("Utilisateur introuvable pour id : " + idUserprofile));
    }

}
