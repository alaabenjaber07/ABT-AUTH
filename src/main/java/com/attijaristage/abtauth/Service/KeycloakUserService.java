package com.attijaristage.abtauth.Service;

import com.attijaristage.abtauth.DTO.UserProfileDTO;
import jakarta.ws.rs.core.Response;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

import static org.keycloak.OAuth2Constants.CLIENT_SECRET;
import static org.keycloak.events.admin.ResourceType.REALM;

@Service
public class KeycloakUserService {
    private static final String SERVER_URL = "http://localhost:8180";
    private static final String TARGET_REALM = "carthago-realm";
    // Realm d'authentification admin
   // private static final String AUTH_REALM = "master";
    private static final String CLIENT_ID = "carthago-client"; // Ton vrai client_id
    private static final String USERNAME = "admin";         // Ton utilisateur admin
    private static final String PASSWORD = "Es3vsyK@dstu4p";             // Son mot de passe

    private final Keycloak keycloak;


        public KeycloakUserService() {
            keycloak = KeycloakBuilder.builder()
                    .serverUrl("http://localhost:8180")
                    .realm("carthago-realm")                         //  ici tu mets carthago-realm car l'admin est dans ce realm
                    .grantType(OAuth2Constants.CLIENT_CREDENTIALS)
                    .clientId(CLIENT_ID)
                    .clientSecret("KJSiuA8GNaXmZvKyucyCF7ngls0JpaFQ")
                    .build();
        }
    public String createUser(String username, String email, String firstName, String lastName, String password) {
        UserRepresentation user = new UserRepresentation();
        user.setUsername(username);
        user.setEmail(email);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setEnabled(true);

        Response response = keycloak.realm("carthago-realm").users().create(user);

        if (response.getStatus() != 201) {
            throw new RuntimeException("❌ Erreur création utilisateur Keycloak: HTTP " + response.getStatus());
        }

        String userId = getCreatedId(response);

        CredentialRepresentation credential = new CredentialRepresentation();
        credential.setTemporary(false);
        credential.setType(CredentialRepresentation.PASSWORD);
        credential.setValue(password);

        keycloak.realm("carthago-realm").users().get(userId).resetPassword(credential);

        return userId;
    }

    private String getCreatedId(Response response) {
        String path = response.getLocation().getPath();
        return path.substring(path.lastIndexOf('/') + 1);
    }



    public String login(String username, String password) {
        Keycloak keycloakLogin = KeycloakBuilder.builder()
                .serverUrl("http://localhost:8180")
                .realm("carthago-realm")
                .grantType(OAuth2Constants.CLIENT_CREDENTIALS)
                .clientId(CLIENT_ID)
                .clientSecret("KJSiuA8GNaXmZvKyucyCF7ngls0JpaFQ")
                .username(username)
                .password(password)
                .build();

        return keycloakLogin.tokenManager().getAccessTokenString();
    }


    public List<UserRepresentation> getAllUsers() {
        return keycloak.realm("carthago-realm").users().list();
    }

    public void deleteUserById(String keycloakId) {
        try {
            keycloak.realm(TARGET_REALM).users().get(keycloakId).remove();
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la suppression dans Keycloak : " + e.getMessage());
        }
    }


    public UserRepresentation getUserById(String keycloakId) {
        try {
            return keycloak.realm(TARGET_REALM).users().get(keycloakId).toRepresentation();
        } catch (Exception e) {
            System.err.println("Erreur récupération user Keycloak : " + e.getMessage());
            return null;
        }
    }

    public void updateUser(String id, String firstName, String lastName, String email) {
        if (id == null || id.isEmpty()) {
            throw new IllegalArgumentException("❌ keycloakId est null ou vide !");
        }

        UserRepresentation user = keycloak.realm(TARGET_REALM)
                .users()
                .get(id)
                .toRepresentation();

        // Ne modifie PAS username ici
        // user.setUsername(username);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setEmail(email);

        keycloak.realm(TARGET_REALM).users().get(id).update(user);
    }






}
