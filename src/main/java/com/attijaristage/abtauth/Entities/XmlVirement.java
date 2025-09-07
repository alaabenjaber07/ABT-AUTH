package com.attijaristage.abtauth.Entities;

import com.attijaristage.abtauth.Entities.Adapter.LocalDateAdapter;
import jakarta.xml.bind.annotation.*;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import java.math.BigDecimal;
import java.time.LocalDate;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "VirementType", propOrder = {
        "id", "guichet", "dateVirement", "nomDonneur",
        "compteSource", "montant", "devise",
        "beneficiaireNom", "beneficiaireAdresse",
        "compteDestination", "instructions", "modeVirement", "status"
})
@XmlRootElement(name = "Virement", namespace = "urn:bfi:tunisia:virement")
public class XmlVirement {

    @XmlElement(namespace = "urn:bfi:tunisia:virement")
    private Long id;

    @XmlElement(required = true, namespace = "urn:bfi:tunisia:virement")
    private String guichet;

    @XmlElement(required = true, namespace = "urn:bfi:tunisia:virement")
    @XmlJavaTypeAdapter(LocalDateAdapter.class)
    private LocalDate dateVirement;


    @XmlElement(required = true, namespace = "urn:bfi:tunisia:virement")
    private String nomDonneur;

    @XmlElement(required = true, namespace = "urn:bfi:tunisia:virement")
    private String compteSource;

    @XmlElement(required = true, namespace = "urn:bfi:tunisia:virement")
    private BigDecimal montant;

    @XmlElement(required = true, namespace = "urn:bfi:tunisia:virement")
    private String devise = "TND";

    @XmlElement(required = true, namespace = "urn:bfi:tunisia:virement")
    private String beneficiaireNom;

    @XmlElement(namespace = "urn:bfi:tunisia:virement")
    private String beneficiaireAdresse;

    @XmlElement(required = true, namespace = "urn:bfi:tunisia:virement")
    private String compteDestination;

    @XmlElement(required = true, namespace = "urn:bfi:tunisia:virement")
    private String instructions; // URGENT | STANDARD | RETARDABLE

    @XmlElement(required = true, namespace = "urn:bfi:tunisia:virement")
    private String modeVirement; // IMMEDIAT | DIFFERE | PERMANENT

    @XmlElement(required = true, namespace = "urn:bfi:tunisia:virement")
    private String status; // EN_ATTENTE | TRAITE | REJETE

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

    public String getDevise() {
        return devise;
    }

    public void setDevise(String devise) {
        this.devise = devise;
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

    public String getInstructions() {
        return instructions;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }

    public String getModeVirement() {
        return modeVirement;
    }

    public void setModeVirement(String modeVirement) {
        this.modeVirement = modeVirement;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}