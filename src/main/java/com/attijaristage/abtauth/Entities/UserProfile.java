package com.attijaristage.abtauth.Entities;
import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name="user_profile")
public class UserProfile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String idKeycloak;
    private Date dateOfBirth;
    private String phoneNumber;
    public UserProfile() {
    }

    public UserProfile(Long id, String idKeycloak, Date dateOfBirth, String phoneNumber) {
        this.id = id;
        this.idKeycloak = idKeycloak;
        this.dateOfBirth = dateOfBirth;
        this.phoneNumber = phoneNumber;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIdKeycloak() {
        return idKeycloak;
    }

    public void setIdKeycloak(String idKeycloak) {
        this.idKeycloak = idKeycloak;
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
