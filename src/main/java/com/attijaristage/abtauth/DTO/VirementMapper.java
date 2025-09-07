package com.attijaristage.abtauth.DTO;

import com.attijaristage.abtauth.Entities.Virement;

public class VirementMapper {
    public static VirementDTO toDTO(Virement entity) {
        if (entity == null) return null;
        VirementDTO dto = new VirementDTO();
        dto.setId(entity.getId()); // AJOUT
        dto.setGuichet(entity.getGuichet());
        dto.setDateVirement(entity.getDateVirement());
        dto.setNomDonneur(entity.getNomDonneur());
        dto.setCompteSource(entity.getCompteSource());
        dto.setMontant(entity.getMontant());
        dto.setBeneficiaireNom(entity.getBeneficiaireNom());
        dto.setBeneficiaireAdresse(entity.getBeneficiaireAdresse());
        dto.setCompteDestination(entity.getCompteDestination());
        dto.setInstructions(entity.getInstructions());
        dto.setModeVirement(entity.getModeVirement());
        dto.setStatus(entity.getStatus());
        dto.setUser(entity.getUser());
        return dto;
    }

    public static Virement toEntity(VirementDTO dto) {
        if (dto == null) return null;
        Virement entity = new Virement();
        entity.setId(dto.getId()); // AJOUT
        entity.setGuichet(dto.getGuichet());
        entity.setDateVirement(dto.getDateVirement());
        entity.setNomDonneur(dto.getNomDonneur());
        entity.setCompteSource(dto.getCompteSource());
        entity.setMontant(dto.getMontant());
        entity.setBeneficiaireNom(dto.getBeneficiaireNom());
        entity.setBeneficiaireAdresse(dto.getBeneficiaireAdresse());
        entity.setCompteDestination(dto.getCompteDestination());
        entity.setInstructions(dto.getInstructions());
        entity.setModeVirement(dto.getModeVirement());
        entity.setStatus(dto.getStatus());
        entity.setUser(dto.getUser());
        return entity;
    }

}