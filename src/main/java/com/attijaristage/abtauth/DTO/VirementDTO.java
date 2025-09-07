package com.attijaristage.abtauth.DTO;

import com.attijaristage.abtauth.Entities.Enum.InstructionVirement;
import com.attijaristage.abtauth.Entities.Enum.ModeVirement;
import com.attijaristage.abtauth.Entities.Enum.StatusVirement;
import com.attijaristage.abtauth.Entities.UserProfile;

import java.math.BigDecimal;
import java.time.LocalDate;

public class VirementDTO {
    private Long id;
    private String guichet;
    private LocalDate dateVirement;
    private String nomDonneur;
    private String compteSource;
    private BigDecimal montant;
    private String beneficiaireNom;
    private String beneficiaireAdresse;
    private String compteDestination;
    private InstructionVirement instructions;
    private ModeVirement modeVirement;
    private StatusVirement status;
    private UserProfile user;
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getGuichet() {
        return guichet;
    }

    public void setGuichet(String guichet) {
        this.guichet = guichet;
    }

    public LocalDate getDateVirement() {
        return dateVirement;
    }

    public void setDateVirement(LocalDate dateVirement) {
        this.dateVirement = dateVirement;
    }

    public String getNomDonneur() {
        return nomDonneur;
    }

    public void setNomDonneur(String nomDonneur) {
        this.nomDonneur = nomDonneur;
    }

    public String getCompteSource() {
        return compteSource;
    }

    public void setCompteSource(String compteSource) {
        this.compteSource = compteSource;
    }

    public BigDecimal getMontant() {
        return montant;
    }

    public void setMontant(BigDecimal montant) {
        this.montant = montant;
    }

    public String getBeneficiaireNom() {
        return beneficiaireNom;
    }

    public void setBeneficiaireNom(String beneficiaireNom) {
        this.beneficiaireNom = beneficiaireNom;
    }

    public String getBeneficiaireAdresse() {
        return beneficiaireAdresse;
    }

    public void setBeneficiaireAdresse(String beneficiaireAdresse) {
        this.beneficiaireAdresse = beneficiaireAdresse;
    }

    public String getCompteDestination() {
        return compteDestination;
    }

    public void setCompteDestination(String compteDestination) {
        this.compteDestination = compteDestination;
    }

    public InstructionVirement getInstructions() {
        return instructions;
    }

    public void setInstructions(InstructionVirement instructions) {
        this.instructions = instructions;
    }

    public ModeVirement getModeVirement() {
        return modeVirement;
    }

    public void setModeVirement(ModeVirement modeVirement) {
        this.modeVirement = modeVirement;
    }

    public StatusVirement getStatus() {
        return status;
    }

    public void setStatus(StatusVirement status) {
        this.status = status;
    }

    public UserProfile getUser() {
        return user;
    }

    public void setUser(UserProfile user) {
        this.user = user;
    }
}
