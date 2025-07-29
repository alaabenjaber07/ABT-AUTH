package com.attijaristage.abtauth.Entities;
import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name="user_profile")
public class UserProfile {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long idUserprofile;
    private String keycloakId;
    private Date dateOfBirth;
    private String matricule;
    private String address;
    private String phoneNumber;

    public UserProfile() {
    }


    public UserProfile(Long id, String idKeycloak, Date dateOfBirth, String matricule, String address, String phoneNumber) {
        this.idUserprofile = id;
        this.keycloakId = idKeycloak;
        this.dateOfBirth = dateOfBirth;
        this.matricule = matricule;
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
    public Long getIdUserProfile() {
        return idUserprofile;
    }

    public void setIdUserProfile(Long id) {
        this.idUserprofile = id;
    }
    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    @Override
    public String toString() {
        return "UserProfile{" +
                "idUserprofile=" + idUserprofile +
                ", keycloakId='" + keycloakId + '\'' +
                ", dateOfBirth=" + dateOfBirth +
                ", matricule='" + matricule + '\'' +
                ", address='" + address + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                '}';
    }
}
