package com.attijaristage.abtauth.DTO;

import com.attijaristage.abtauth.Entities.XmlVirement;

public class XmlVirementMapper {
    public static XmlVirement fromDto(VirementDTO dto) {
        XmlVirement x = new XmlVirement();
        x.setId(dto.getId());
        x.setGuichet(dto.getGuichet());
        x.setDateVirement(dto.getDateVirement());
        x.setNomDonneur(dto.getNomDonneur());
        x.setCompteSource(dto.getCompteSource());
        x.setMontant(dto.getMontant());
        x.setDevise("TND");
        x.setBeneficiaireNom(dto.getBeneficiaireNom());
        x.setBeneficiaireAdresse(dto.getBeneficiaireAdresse());
        x.setCompteDestination(dto.getCompteDestination());
        x.setInstructions(dto.getInstructions() != null ? dto.getInstructions().name() : null);
        x.setModeVirement(dto.getModeVirement() != null ? dto.getModeVirement().name() : null);
        x.setStatus(dto.getStatus() != null ? dto.getStatus().name() : null);
        return x;
    }
}