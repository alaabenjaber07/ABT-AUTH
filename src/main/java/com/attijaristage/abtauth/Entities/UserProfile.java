package com.attijaristage.abtauth.Entities;
import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name="user_profile")
public class UserProfile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idUserprofile;
    private String keycloakId;
    private Date dateOfBirth;
    private String matricule;
    private String address;
    private String phoneNumber;
    public UserProfile() {
    }

    public String getMatricule() {
        return matricule;
    }

    public void setMatricule(String matricule) {
        this.matricule = matricule;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public UserProfile(Long id, String idKeycloak, Date dateOfBirth, String matricule, String address, String phoneNumber) {
        this.idUserprofile = id;
        this.keycloakId = idKeycloak;
        this.dateOfBirth = dateOfBirth;
        this.matricule = matricule;
        this.address = address;
        this.phoneNumber = phoneNumber;
    }



    public Long getIdUserProfile() {
        return idUserprofile;
    }

    public void setIdUserProfile(Long id) {
        this.idUserprofile = id;
    }

    public String getKeycloakId() {
        return keycloakId;
    }

    public void setKeycloakId(String idKeycloak) {
        this.keycloakId= idKeycloak;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
