package com.attijaristage.abtauth.DTO;

import com.attijaristage.abtauth.Entities.Effets;
import com.attijaristage.abtauth.Entities.Enum.EtatEffet;
import com.attijaristage.abtauth.Entities.Enum.TypeEffet;

public class EffetsMapper {
    public static EffetsDTO toDTO(Effets effet) {
        EffetsDTO dto = new EffetsDTO();
        dto.setIdEffet(effet.getIdEffet());
        dto.setDateEcheance(effet.getDateEcheance());
        dto.setEtat(effet.getEtat().toString());
        dto.setDateEmission(effet.getDateEmission());
        dto.setMontant(effet.getMontant());
        dto.setReference(effet.getReference());
        dto.setTire(effet.getTire());
        dto.setTireur(effet.getTireur());
        dto.setIbanTire(effet.getIbanTire());
        dto.setIbanTireur(effet.getIbanTireur());
        dto.setBeneficiaire(effet.getBeneficiaire());
        dto.setDateAjout(effet.getDateAjout());
        dto.setTypeEffet(effet.getTypeEffet().toString());
        dto.setUser(effet.getUser());
        return dto;
    }
    public static Effets toEntity(EffetsDTO dto){
        Effets effets=new Effets();
        effets.setIdEffet(dto.getIdEffet());
        effets.setEtat(EtatEffet.EN_ATTENTE);
        effets.setDateEcheance(dto.getDateEcheance());
        effets.setMontant(dto.getMontant());
        effets.setReference(dto.getReference());
        effets.setDateEmission(dto.getDateEmission());
        effets.setTireur(dto.getTireur());
        if(dto.getTypeEffet().equals("BILLET_A_ORDRE")) {
            effets.setTire(null);
        } else {
            effets.setTire(dto.getTire());
        }
        effets.setIbanTireur(dto.getIbanTireur());
        effets.setIbanTire(dto.getIbanTire());
        effets.setBeneficiaire(dto.getBeneficiaire());
        effets.setTypeEffet(Enum.valueOf(TypeEffet.class,dto.getTypeEffet()));
        effets.setDateAjout(dto.getDateAjout());
        effets.setUser(dto.getUser());
        return effets;
    }
}
