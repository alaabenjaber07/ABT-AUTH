package com.attijaristage.abtauth.DTO;

import jakarta.validation.constraints.Email;
import lombok.Data;

import java.util.Date;

@Data
public class UserProfileDTO {
    private String username;
    private String password;
    @Email
    private String email;
    private String firstName;
    private String lastName;
    private Date dateOfBirth;
    private String phoneNumber;
    private String idKeycloak;
    public UserProfileDTO() {
    }

    public UserProfileDTO(String username, String password, String email, String firstName, String lastName, Date dateOfBirth, String phoneNumber, String idKeycloak) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
        this.phoneNumber = phoneNumber;
        this.idKeycloak = idKeycloak;
    }

    public UserProfileDTO(Date dateOfBirth, String phoneNumber, String idKeycloak) {
        this.idKeycloak = idKeycloak;
        this.dateOfBirth = dateOfBirth;
        this.phoneNumber = phoneNumber;
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

    public String getIdKeycloak() {
        return idKeycloak;
    }

    public void setIdKeycloak(String idKeycloak) {
        this.idKeycloak = idKeycloak;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}
