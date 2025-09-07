package com.attijaristage.abtauth.Entities;

import com.attijaristage.abtauth.Entities.Enum.InstructionVirement;
import com.attijaristage.abtauth.Entities.Enum.ModeVirement;
import com.attijaristage.abtauth.Entities.Enum.StatusVirement;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
public class Virement {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    private String guichet;

    @Column(name = "date_virement")
    private LocalDate dateVirement;

    private String nomDonneur;

    private String compteSource;

    private BigDecimal montant;

    private String beneficiaireNom;

    private String beneficiaireAdresse;

    private String compteDestination;

    @Enumerated(EnumType.STRING)
    private InstructionVirement instructions;

    @Enumerated(EnumType.STRING)
    private ModeVirement modeVirement;

    @Enumerated(EnumType.STRING)
    private StatusVirement status = StatusVirement.EN_ATTENTE;

    @ManyToOne
    @JoinColumn(name = "user_id") // clé étrangère vers User
    private UserProfile user;

    public Virement(Long id, String guichet, LocalDate dateVirement, String nomDonneur, String compteSource, BigDecimal montant, String beneficiaireNom, String beneficiaireAdresse, String compteDestination, InstructionVirement instructions, ModeVirement modeVirement, StatusVirement status) {
        this.id = id;
        this.guichet = guichet;
        this.dateVirement = dateVirement;
        this.nomDonneur = nomDonneur;
        this.compteSource = compteSource;
        this.montant = montant;
        this.beneficiaireNom = beneficiaireNom;
        this.beneficiaireAdresse = beneficiaireAdresse;
        this.compteDestination = compteDestination;
        this.instructions = instructions;
        this.modeVirement = modeVirement;
        this.status = status;
    }

    public Virement(Long id, String guichet, LocalDate dateVirement, String nomDonneur, String compteSource, BigDecimal montant, String beneficiaireNom, String beneficiaireAdresse, String compteDestination, InstructionVirement instructions, ModeVirement modeVirement, StatusVirement status, UserProfile user) {
        this.id = id;
        this.guichet = guichet;
        this.dateVirement = dateVirement;
        this.nomDonneur = nomDonneur;
        this.compteSource = compteSource;
        this.montant = montant;
        this.beneficiaireNom = beneficiaireNom;
        this.beneficiaireAdresse = beneficiaireAdresse;
        this.compteDestination = compteDestination;
        this.instructions = instructions;
        this.modeVirement = modeVirement;
        this.status = status;
        this.user = user;
    }

    public Virement() {
    }

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
