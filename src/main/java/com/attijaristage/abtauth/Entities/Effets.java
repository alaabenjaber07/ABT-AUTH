package com.attijaristage.abtauth.Entities;


import com.attijaristage.abtauth.Entities.Adapter.LocalDateAdapter;
import com.attijaristage.abtauth.Entities.Enum.EtatEffet;
import com.attijaristage.abtauth.Entities.Enum.TypeEffet;
import jakarta.persistence.*;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import java.math.BigDecimal;
import java.time.LocalDate;


@Entity
@Table(name="EFFETS")
public class Effets {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idEffet;
    private String reference;
    private String tireur; //creancier
    private String tire; //debiteur
    private String beneficiaire;
    private BigDecimal montant;
    private LocalDate dateEmission;
    private LocalDate dateEcheance;
    private EtatEffet etat;
    private String ibanTireur;
    private String ibanTire;
    @Enumerated(EnumType.STRING)
    private TypeEffet typeEffet;
    private LocalDate dateAjout;
    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "idUserprofile") // clé étrangère vers User
    private UserProfile user;
    public Effets(){}

    public Effets(Long idEffet, String reference, String tireur, String tire, String beneficiaire, BigDecimal montant, LocalDate dateEmission, LocalDate dateEcheance, EtatEffet etat, String ibanTireur, String ibanTire, TypeEffet typeEffet, LocalDate dateAjout, UserProfile user) {
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
        this.typeEffet = typeEffet;
        this.dateAjout = dateAjout;
        this.user = user;
    }

    public Effets(Long idEffet, String reference, String tireur, String tire, String beneficiaire, BigDecimal montant, LocalDate dateEmission, LocalDate dateEcheance, EtatEffet etat, String ibanTireur, String ibanTire, TypeEffet typeEffet, LocalDate dateAjout) {
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
        this.typeEffet = typeEffet;
        this.dateAjout = dateAjout;
    }

    public Effets(Long idEffet, String reference, String tireur, String tire, String beneficiaire, BigDecimal montant, LocalDate dateEmission, LocalDate dateEcheance, EtatEffet etat, String ibanTireur, String ibanTire, TypeEffet typeEffet) {
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
        this.typeEffet = typeEffet;
    }

    public Effets(Long idEffet, String reference, String tireur, String tire, BigDecimal montant, LocalDate dateEmission, LocalDate dateEcheance, EtatEffet etat, String ibanTireur, String ibanTire, TypeEffet typeEffet) {
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
        this.typeEffet = typeEffet;
    }

    public Effets(Long idEffet, String reference, String tireur, String tire, BigDecimal montant, LocalDate dateEmission, LocalDate dateEcheance, EtatEffet etat, String ibanTireur, String ibanTire) {
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

    public EtatEffet getEtat() {
        return etat;
    }

    public void setEtat(EtatEffet etat) {
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

    public TypeEffet getTypeEffet() {
        return typeEffet;
    }

    public void setTypeEffet(TypeEffet typeEffet) {
        this.typeEffet = typeEffet;
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

    public UserProfile getUser() {
        return user;
    }

    public void setUser(UserProfile user) {
        this.user = user;
    }
}
