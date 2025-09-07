package com.attijaristage.abtauth.DTO;

import com.attijaristage.abtauth.Entities.Adapter.LocalDateAdapter;
import com.attijaristage.abtauth.Entities.UserProfile;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import java.math.BigDecimal;
import java.time.LocalDate;
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "effet")
public class EffetsDTO {
    private Long idEffet;
    private String reference;
    private String tireur;
    private String tire;
    private String beneficiaire;
    private BigDecimal montant;
    @XmlJavaTypeAdapter(LocalDateAdapter.class)
    private LocalDate dateEmission;
    @XmlJavaTypeAdapter(LocalDateAdapter.class)
    private LocalDate dateEcheance;
    private String etat;
    private String ibanTireur;
    private String ibanTire;
    @XmlJavaTypeAdapter(LocalDateAdapter.class)
    private LocalDate dateAjout;
    private String typeEffet;
    private UserProfile user;
    public EffetsDTO() {

    }

    public EffetsDTO(Long idEffet, String reference, String tireur, String tire, String beneficiaire, BigDecimal montant, LocalDate dateEmission, LocalDate dateEcheance, String etat, String ibanTireur, String ibanTire, LocalDate dateAjout, String typeEffet, UserProfile user) {
        this.idEffet = idEffet;
        this.reference = reference;
        this.tireur = tireur;
        this.tire = tire;
        this.beneficiaire = beneficiaire;
        this.montant = montant;
        this.dateEmission = dateEmission;
        this.dateEcheance = dateEcheance;
        this.etat = etat;
        this.ibanTireur = ibanTireur;
        this.ibanTire = ibanTire;
        this.dateAjout = dateAjout;
        this.typeEffet = typeEffet;
        this.user = user;
    }

    public EffetsDTO(Long idEffet, String reference, String tireur, String tire, BigDecimal montant, LocalDate dateEmission, LocalDate dateEcheance, String etat, String ibanTireur, String ibanTire) {
        this.idEffet = idEffet;
        this.reference = reference;
        this.tireur = tireur;
        this.tire = tire;
        this.montant = montant;
        this.dateEmission = dateEmission;
        this.dateEcheance = dateEcheance;
        this.etat = etat;
        this.ibanTireur = ibanTireur;
        this.ibanTire = ibanTire;
    }

    public Long getIdEffet() {
        return idEffet;
    }

    public void setIdEffet(Long idEffet) {
        this.idEffet = idEffet;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getTireur() {
        return tireur;
    }

    public void setTireur(String tireur) {
        this.tireur = tireur;
    }

    public String getTire() {
        return tire;
    }

    public void setTire(String tire) {
        this.tire = tire;
    }

    public BigDecimal getMontant() {
        return montant;
    }

    public void setMontant(BigDecimal montant) {
        this.montant = montant;
    }

    public LocalDate getDateEmission() {
        return dateEmission;
    }

    public void setDateEmission(LocalDate dateEmission) {
        this.dateEmission = dateEmission;
    }

    public LocalDate getDateEcheance() {
        return dateEcheance;
    }

    public void setDateEcheance(LocalDate dateEcheance) {
        this.dateEcheance = dateEcheance;
    }

    public String getEtat() {
        return etat;
    }

    public void setEtat(String etat) {
        this.etat = etat;
    }

    public String getIbanTireur() {
        return ibanTireur;
    }

    public void setIbanTireur(String ibanTireur) {
        this.ibanTireur = ibanTireur;
    }

    public String getIbanTire() {
        return ibanTire;
    }

    public void setIbanTire(String ibanTire) {
        this.ibanTire = ibanTire;
    }

    public String getBeneficiaire() {
        return beneficiaire;
    }

    public void setBeneficiaire(String beneficiaire) {
        this.beneficiaire = beneficiaire;
    }

    public LocalDate getDateAjout() {
        return dateAjout;
    }

    public void setDateAjout(LocalDate dateAjout) {
        this.dateAjout = dateAjout;
    }

    public String getTypeEffet() {
        return typeEffet;
    }

    public void setTypeEffet(String typeEffet) {
        this.typeEffet = typeEffet;
    }

    public UserProfile getUser() {
        return user;
    }

    public void setUser(UserProfile user) {
        this.user = user;
    }
}
