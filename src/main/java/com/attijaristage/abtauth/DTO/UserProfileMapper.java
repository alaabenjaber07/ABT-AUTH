package com.attijaristage.abtauth.DTO;

import com.attijaristage.abtauth.Entities.UserProfile;

public class UserProfileMapper {
    public static UserProfileDTO toDTO(UserProfile entity) {
        if (entity == null) return null;
        return new UserProfileDTO(
                entity.getKeycloakId(),
                entity.getMatricule(),
                entity.getAddress(),
                entity.getPhoneNumber()
        );
    }

    public static UserProfile toEntity(UserProfileDTO dto) {
        if (dto == null) return null;
        UserProfile entity = new UserProfile();
        entity.setKeycloakId(dto.getKeycloakId());
        entity.setMatricule(dto.getMatricule());
        entity.setAddress(dto.getAddress());
        entity.setPhoneNumber(dto.getPhoneNumber());
        return entity;
    }
}
