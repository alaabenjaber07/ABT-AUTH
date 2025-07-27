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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.keycloak.representations.idm.RoleRepresentation;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class KeycloakUserService {/*
    private static final String SERVER_URL = "http://localhost:8180";
    private static final String TARGET_REALM = "carthago-realm";*/
    // Realm d'authentification admin
   // private static final String AUTH_REALM = "master";
    private static final String CLIENT_ID = "carthago-client"; // Ton vrai client_id
    /*private static final String USERNAME = "admin";         // Ton utilisateur admin
    private static final String PASSWORD = "Es3vsyK@dstu4p";             // Son mot de passe
*/
    private final Keycloak keycloak;
    private final String realm = "carthago-realm";
    @Value("${keycloak.client-secret}")
    String clientSecret;
    private String clientId="carthago-client";
    public KeycloakUserService(@Value("${keycloak.client-secret}") String clientSecret, @Value("${keycloak.admin-username}") String adminUsername,
                               @Value("${keycloak.admin-password}") String adminPassword ) {
        this.keycloak = KeycloakBuilder.builder()
                .serverUrl("http://localhost:8080")  //URL of your Keycloak server
                .realm(realm)                    // Realm used to authenticate (usually "master" for admin users)
                .clientId(clientId)
                .clientSecret(clientSecret)// Client ID with admin rights in that realm (usually "admin-cli")
                .username(adminUsername)                  // Admin username
                .password(adminPassword)                  // Admin password
                .grantType(OAuth2Constants.PASSWORD) // We're using password-based authentication
                .build();
    }

    public Keycloak getKeycloak() {
        return keycloak;
    }

    public void deleteUserInKeycloak(String keycloakId) {
        if (keycloakId != null && !keycloakId.isBlank()) {
            keycloak.realm(realm).users().get(keycloakId).remove();
        } else {
            throw new IllegalArgumentException("L'identifiant Keycloak est null ou vide.");
        }
    }
        public void validateUniqueEmail(String email, String currentUserId) {
            List<UserRepresentation> usersWithSameEmail = keycloak.realm(realm)
                    .users()
                    .searchByEmail(email, true);

            for (UserRepresentation user : usersWithSameEmail) {
                // Vérifie que l'utilisateur trouvé n'est pas celui actuellement modifié (si update)
                if (!user.getId().equals(currentUserId) && email.equalsIgnoreCase(user.getEmail())) {
                    throw new RuntimeException("L'email '" + email + "' est déjà utilisé.");
                }
            }
        }
    public String createUser(String username, String email, String firstName, String lastName, String password) {
        UserRepresentation user = new UserRepresentation();
        user.setUsername(username);
        user.setEmail(email);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setEnabled(true);

        Response response = keycloak.realm("carthago-realm").users().create(user);
        if(response.getStatus()==409){
            throw new RuntimeException("Utilisateur existant! Veuillez Modifier l'addresse Email ou Username" );

        }else {
            if (response.getStatus() != 201) {
                throw new RuntimeException("Erreur création utilisateur Keycloak: HTTP " + response.getStatus());
            }
        }

        String userId = getCreatedId(response);

        CredentialRepresentation credential = new CredentialRepresentation();
        credential.setTemporary(false);
        credential.setType(CredentialRepresentation.PASSWORD);
        credential.setValue(password);

        keycloak.realm("carthago-realm").users().get(userId).resetPassword(credential);

        return userId;
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

            // Valide l'unicité mail avant mise à jour
            if (!newEmail.equalsIgnoreCase(user.getEmail())) {
                validateUniqueEmail(newEmail, keycloakId);
            }

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

    public String getUserRoleByKeycloakId(String keycloakId) {
        try {
            List<String> roles = keycloak.realm(realm)
                    .users()
                    .get(keycloakId)
                    .roles()
                    .realmLevel()
                    .listEffective()
                    .stream()
                    .map(role -> role.getName())
                    .collect(Collectors.toList());

            if (roles.contains("admin")) {
                return "admin";
            } else if (roles.contains("bancaire")) {
                return "bancaire";
            } else {
                return "aucun rôle";
            }
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la récupération du rôle de l'utilisateur " + keycloakId, e);
        }
    }
    private String getCreatedId(Response response) {
        String path = response.getLocation().getPath();
        return path.substring(path.lastIndexOf('/') + 1);
    }
    public String login(String username, String password) {
        Keycloak keycloakLogin = KeycloakBuilder.builder()
                .serverUrl("http://localhost:8080")
                .realm("carthago-realm")
                .grantType(OAuth2Constants.PASSWORD)
                .clientId(CLIENT_ID)
                .clientSecret(clientSecret)
                .username(username)
                .password(password)
                .build();

        return keycloakLogin.tokenManager().getAccessTokenString();
    }

    public void setUserRole(String keycloakId, String roleName) {
        if (!List.of("admin", "bancaire").contains(roleName)) {
            throw new IllegalArgumentException("Rôle invalide : " + roleName);
        }
        RoleRepresentation role = keycloak.realm(realm).roles().get(roleName).toRepresentation();
        List<RoleRepresentation> currentRoles = keycloak.realm(realm).users().get(keycloakId).roles().realmLevel().listEffective();
        List<RoleRepresentation> toRemove = currentRoles.stream()
                .filter(r -> r.getName().equals("admin") || r.getName().equals("bancaire"))
                .collect(Collectors.toList());
        keycloak.realm(realm).users().get(keycloakId).roles().realmLevel().remove(toRemove);
        keycloak.realm(realm)
                .users()
                .get(keycloakId)
                .roles()
                .realmLevel()
                .add(List.of(role));
    }

    public List<UserRepresentation> getAllUsers() {
        return keycloak.realm("carthago-realm").users().list();
    }

    public UserRepresentation getUserById(String keycloakId) {
        try {
            return keycloak.realm(realm).users().get(keycloakId).toRepresentation();
        } catch (Exception e) {
            System.err.println("Erreur récupération user Keycloak : " + e.getMessage());
            return null;
        }
    }

}
