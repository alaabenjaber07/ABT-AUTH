package com.attijaristage.abtauth.DTO;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import lombok.Data;

import java.util.Date;

@Data
public class UserProfileDTO {
    private String username;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;
    @Email
    private String email;
    private String firstName;
    private String lastName;
    private Date dateOfBirth;
    private String matricule;
    private String address;
    private String phoneNumber;
    private String keycloakId;
    public UserProfileDTO(String keycloakId, String matricule, String address, String phoneNumber, Date dateOfBirth) {
    }

    public UserProfileDTO(String username, String password, String email, String firstName, String lastName, Date dateOfBirth, String matricule, String address, String phoneNumber, String keycloakId) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
        this.matricule = matricule;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.keycloakId = keycloakId;
    }

    public UserProfileDTO(Date dateOfBirth, String phoneNumber, String idKeycloak) {
        this.keycloakId = idKeycloak;
        this.dateOfBirth = dateOfBirth;
        this.phoneNumber = phoneNumber;
    }

    public UserProfileDTO(String keycloakId, String matricule, String address, String phoneNumber) {
        this.keycloakId=keycloakId;
        this.matricule=matricule;
        this.address=address;
        this.phoneNumber=phoneNumber;
    }

    public UserProfileDTO() {}


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

    public String getKeycloakId() {
        return keycloakId;
    }

    public void setKeycloakId(String idKeycloak) {
        this.keycloakId= idKeycloak;
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

    @Override
    public String toString() {
        return "UserProfileDTO{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", dateOfBirth=" + dateOfBirth +
                ", matricule='" + matricule + '\'' +
                ", address='" + address + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", keycloakId='" + keycloakId + '\'' +
                '}';
    }

}
