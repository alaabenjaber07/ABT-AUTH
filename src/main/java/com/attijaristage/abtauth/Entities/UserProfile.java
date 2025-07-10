package com.attijaristage.abtauth.Entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import org.springframework.boot.autoconfigure.domain.EntityScan;

import java.util.Date;

@Entity
public class UserProfile {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long idUserprofile;
    private String keycloakId; // Pour faire le lien avec Keycloak userId
    private String matricule;
    private String address;
    private String phoneNumber;

    public UserProfile() {
    }


    public UserProfile(String matricule, String keycloakId, String address,  String phoneNumber) {
        this.matricule = matricule;
        this.keycloakId = keycloakId;
        this.address = address;
        this.phoneNumber = phoneNumber;
    }

    public String getMatricule() {
        return matricule;
    }

    public void setMatricule(String matricule) {
        this.matricule = matricule;
    }

    public String getKeycloakId() {
        return keycloakId;
    }

    public void setKeycloakId(String keycloakId) {
        this.keycloakId = keycloakId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }


    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @Override
    public String toString() {
        return "UserProfile{" +
                "matricule='" + matricule + '\'' +
                ", keycloakId='" + keycloakId + '\'' +
                ", address='" + address + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                '}';
    }
}
