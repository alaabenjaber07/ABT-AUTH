package com.attijaristage.abtauth.Service;


import com.attijaristage.abtauth.DTO.UserProfileDTO;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.core.Response;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.CreatedResponseUtil;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.representations.idm.UserRepresentation;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class KeycloakUserService {
    private final Keycloak keycloak;
    private final String realm = "carthago-realm";

    public KeycloakUserService() {
        this.keycloak = KeycloakBuilder.builder()
                .serverUrl("http://localhost:8080")  //URL of your Keycloak server
                .realm("master")                    // Realm used to authenticate (usually "master" for admin users)
                .clientId("admin-cli")              // Client ID with admin rights in that realm (usually "admin-cli")
                .username("yessminehassad")                  // Admin username
                .password("admin")                  // Admin password
                .grantType(OAuth2Constants.PASSWORD) // We're using password-based authentication
                .build();
    }

    public void deleteUserInKeycloak(String keycloakId) {
        if (keycloakId != null && !keycloakId.isBlank()) {
            keycloak.realm(realm).users().get(keycloakId).remove();
        } else {
            throw new IllegalArgumentException("L'identifiant Keycloak est null ou vide.");
        }
    }
    public void validateUniqueUsernameAndEmail(String username, String email, String currentUserId) {
        List<UserRepresentation> usersWithSameUsername = keycloak.realm(realm)
                .users()
                .search(username, 0, 10);

        for (UserRepresentation user : usersWithSameUsername) {
            if (!user.getId().equals(currentUserId) && username.equalsIgnoreCase(user.getUsername())) {
                throw new RuntimeException("Le nom d'utilisateur '" + username + "' est déjà utilisé.");
            }
        }

        List<UserRepresentation> usersWithSameEmail = keycloak.realm(realm)
                .users()
                .searchByEmail(email, true);

        for (UserRepresentation user : usersWithSameEmail) {
            if (!user.getId().equals(currentUserId) && email.equalsIgnoreCase(user.getEmail())) {
                throw new RuntimeException("L'email '" + email + "' est déjà utilisé.");
            }
        }
    }


    public void updateUserInKeycloak(UserProfileDTO dto, String keycloakId) {
        if (keycloakId == null || keycloakId.isBlank()) {
            throw new IllegalArgumentException("L'identifiant Keycloak est requis.");
        }

        UserResource userResource = keycloak.realm(realm).users().get(keycloakId);
        UserRepresentation user = userResource.toRepresentation();

        // Prépare les valeurs à vérifier (utilise les nouvelles valeurs si fournies, sinon celles existantes)
        String newUsername = (dto.getUsername() != null && !dto.getUsername().isBlank()) ? dto.getUsername().trim() : user.getUsername();
        String newEmail = (dto.getEmail() != null && !dto.getEmail().isBlank()) ? dto.getEmail().trim() : user.getEmail();

        // Valide l'unicité avant mise à jour
        validateUniqueUsernameAndEmail(newUsername, newEmail, keycloakId);

        // Mise à jour des champs uniquement s’ils sont fournis dans dto
        if (dto.getUsername() != null && !dto.getUsername().isBlank()) {
            user.setUsername(newUsername);
        }

        if (dto.getEmail() != null && !dto.getEmail().isBlank()) {
            user.setEmail(newEmail);
        } else if (user.getEmail() == null || user.getEmail().isBlank()) {
            throw new RuntimeException("L'email est requis pour mettre à jour l'utilisateur dans Keycloak.");
        }

        if (dto.getFirstName() != null && !dto.getFirstName().isBlank()) {
            user.setFirstName(dto.getFirstName().trim());
        }
        if (dto.getLastName() != null && !dto.getLastName().isBlank()) {
            user.setLastName(dto.getLastName().trim());
        }

        try {
            userResource.update(user);
        } catch (BadRequestException e) {
            System.err.println("Keycloak update failed:");
            System.err.println("Email: " + user.getEmail());
            System.err.println("Username: " + user.getUsername());
            System.err.println("FirstName: " + user.getFirstName());
            System.err.println("LastName: " + user.getLastName());
            throw new RuntimeException("Échec de mise à jour Keycloak: " + e.getMessage(), e);
        }

        // Mise à jour du mot de passe si besoin
        if (dto.getPassword() != null && !dto.getPassword().isBlank()) {
            CredentialRepresentation cred = new CredentialRepresentation();
            cred.setType(CredentialRepresentation.PASSWORD);
            cred.setValue(dto.getPassword());
            cred.setTemporary(false);
            userResource.resetPassword(cred);
        }
    }


    public String createUserInKeycloak(UserProfileDTO userDto) {
        CredentialRepresentation credential = new CredentialRepresentation();
        credential.setTemporary(false);
        credential.setType(CredentialRepresentation.PASSWORD);
        credential.setValue(userDto.getPassword());

        UserRepresentation user = new UserRepresentation();
        user.setUsername(userDto.getUsername());
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setEmail(userDto.getEmail());
        user.setEnabled(true);

        Response response = keycloak.realm(realm).users().create(user);

        if (response.getStatus() != 201) {
            throw new RuntimeException("Erreur lors de la création de l'utilisateur : Status = " + response.getStatus());
        }

        String userId = CreatedResponseUtil.getCreatedId(response);
        if (userDto.getPassword() == null || userDto.getPassword().isBlank()) {
            throw new RuntimeException("Le mot de passe est requis pour créer l'utilisateur dans Keycloak.");
        }

        // Très important : ne fais resetPassword que si userId est valide
        keycloak.realm(realm).users().get(userId).resetPassword(credential);

        return userId;
    }
    public boolean isUsernameTaken(String username, String excludeUserId) {
        List<UserRepresentation> users = keycloak.realm(realm)
                .users()
                .search(username, 0, 10);

        for (UserRepresentation u : users) {
            if (!u.getId().equals(excludeUserId) && username.equalsIgnoreCase(u.getUsername())) {
                return true;
            }
        }
        return false;
    }
}
