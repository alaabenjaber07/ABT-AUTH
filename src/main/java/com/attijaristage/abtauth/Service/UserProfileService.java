package com.attijaristage.abtauth.Service;

import com.attijaristage.abtauth.DTO.UserProfileDTO;
import com.attijaristage.abtauth.Entities.UserProfile;
import com.attijaristage.abtauth.Repository.UserProfileRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserProfileService {
    @Autowired
    private  UserProfileRepo repo ;
    @Autowired
    private KeycloakUserService keycloakUserService;

    public UserProfile create(UserProfileDTO dto,String keycloakId){
        UserProfile user = new UserProfile();
        user.setIdKeycloak(keycloakId);
        user.setDateOfBirth(dto.getDateOfBirth());
        user.setPhoneNumber(dto.getPhoneNumber());
        return repo.save(user);
    }
    public List<UserProfile> getAll(){
        return repo.findAll();
    }
    public Optional<UserProfile> getById(Long id) {
        return repo.findById(id);
    }
    public void delete(Long id){
        if (repo.existsById(id)){
            repo.deleteById(id);
        }
    }
    /*UPDATE*/
    public Optional<UserProfile> update(Long id, UserProfileDTO dto) {
        return repo.findById(id).map(existingUser -> {
            if (existingUser.getIdKeycloak() != null) {
                keycloakUserService.updateUserInKeycloak(dto, existingUser.getIdKeycloak());
            }
            if (dto.getDateOfBirth() != null) {
                existingUser.setDateOfBirth(dto.getDateOfBirth());
            }
            if (dto.getPhoneNumber() != null) {
                existingUser.setPhoneNumber(dto.getPhoneNumber());
            }
            if (dto.getIdKeycloak() != null) {
                existingUser.setIdKeycloak(dto.getIdKeycloak());

            }

            return repo.save(existingUser);
        });
    }

}
