    package com.attijaristage.abtauth.DTO;

    import java.util.Date;

    public class UserProfileDTO {
        // Infos pour Keycloak
            private Long idUserprofile;  // Ajouté ici
        private String username;
        private String email;
        private String firstName;
        private String lastName;
        private String password;
        // Infos pour ta base
        private String keycloakId;
        private String matricule;
        private String address;
        private String phoneNumber;

        public UserProfileDTO() {
        }

        public UserProfileDTO(String keycloakId, String matricule, String address,  String phoneNumber) {
            this.keycloakId = keycloakId;
            this.matricule = matricule;
            this.address = address;
            this.phoneNumber = phoneNumber;
        }

        public String getKeycloakId() {
            return keycloakId;
        }

        public void setKeycloakId(String keycloakId) {
            this.keycloakId = keycloakId;
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



        public String getPhoneNumber() {
            return phoneNumber;
        }

        public void setPhoneNumber(String phoneNumber) {
            this.phoneNumber = phoneNumber;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
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

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }


        public Long getIdUserprofile() {
            return idUserprofile;
        }

        public void setIdUserprofile(Long idUserprofile) {
            this.idUserprofile = idUserprofile;
        }
    }
