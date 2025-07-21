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
import org.springframework.stereotype.Service;
import org.keycloak.representations.idm.RoleRepresentation;


import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class KeycloakUserService {
    private final Keycloak keycloak;
    private final String realm = "carthago-realm";
    public KeycloakUserService(@Value("${keycloak.client-secret}") String clientSecret, @Value("${keycloak.admin-username}") String adminUsername,
                               @Value("${keycloak.admin-password}") String adminPassword ) {
        this.keycloak = KeycloakBuilder.builder()
                .serverUrl("http://localhost:8080")  //URL of your Keycloak server
                .realm("carthago-realm")                    // Realm used to authenticate (usually "master" for admin users)
                .clientId("carthago-client")
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

    public String createUserInKeycloak(UserProfileDTO userDto) {
        // Validation de l’unicité de l’email
        validateUniqueEmail(userDto.getEmail(), null); // Lors de la création, pas encore d'ID

        // Préparer les informations d'identifiants (mot de passe)
        CredentialRepresentation credential = new CredentialRepresentation();
        credential.setTemporary(false);
        credential.setType(CredentialRepresentation.PASSWORD);
        credential.setValue(userDto.getPassword());

        // Construire l'utilisateur Keycloak
        UserRepresentation user = new UserRepresentation();
        user.setUsername(userDto.getUsername());
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setEmail(userDto.getEmail());
        user.setEnabled(true);

        // Appel API Keycloak pour créer l'utilisateur
        Response response = keycloak.realm(realm).users().create(user);

        if (response.getStatus() != 201) {
            throw new RuntimeException("Erreur lors de la création de l'utilisateur : Status = " + response.getStatus());
        }

        // Récupérer l'ID de l'utilisateur nouvellement créé
        String userId = CreatedResponseUtil.getCreatedId(response);
        RoleRepresentation role= keycloak.realm(realm).roles().get(userDto.getRole()).toRepresentation();
        keycloak.realm(realm)
                .users()
                .get(userId)
                .roles()
                .realmLevel()
                .add(List.of(role));

        // Vérifier la présence du mot de passe
        if (userDto.getPassword() == null || userDto.getPassword().isBlank()) {
            throw new RuntimeException("Le mot de passe est requis pour créer l'utilisateur dans Keycloak.");
        }

        // Définir le mot de passe pour le nouvel utilisateur
        keycloak.realm(realm).users().get(userId).resetPassword(credential);


        return userId;
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

    public void setUserRole(String keycloakId, String roleName) {
        // Vérifie que le rôle est valide
        if (!List.of("admin", "bancaire").contains(roleName)) {
            throw new IllegalArgumentException("Rôle invalide : " + roleName);
        }

        // Récupère le rôle depuis Keycloak
        RoleRepresentation role = keycloak.realm(realm).roles().get(roleName).toRepresentation();
        List<RoleRepresentation> currentRoles = keycloak.realm(realm).users().get(keycloakId).roles().realmLevel().listEffective();
        List<RoleRepresentation> toRemove = currentRoles.stream()
                .filter(r -> r.getName().equals("admin") || r.getName().equals("bancaire"))
                .collect(Collectors.toList());
        keycloak.realm(realm).users().get(keycloakId).roles().realmLevel().remove(toRemove);
        // Assigne le rôle à l'utilisateur
        keycloak.realm(realm)
                .users()
                .get(keycloakId)
                .roles()
                .realmLevel()
                .add(List.of(role));
    }




}
