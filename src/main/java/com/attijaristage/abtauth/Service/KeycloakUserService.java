package com.attijaristage.abtauth.Service;


import com.attijaristage.abtauth.DTO.UserProfileDTO;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.core.Response;
import org.jboss.resteasy.client.jaxrs.internal.ResteasyClientBuilderImpl;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.CreatedResponseUtil;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;
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
import java.util.Collections;
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
                .clientId(clientId)// Client ID with admin rights in that realm (usually "admin-cli")
                .clientSecret(clientSecret)
                //.username(adminUsername)                  // Admin username
                //.password(adminPassword)                  // Admin password
                .grantType(OAuth2Constants.CLIENT_CREDENTIALS)
                .resteasyClient(new ResteasyClientBuilderImpl().connectionPoolSize(10).build())
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
    public String createUser(String username, String email, String firstName, String lastName, String password) {
        UserRepresentation user = new UserRepresentation();
        user.setUsername(username);
        user.setEmail(email);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setEnabled(true);
        System.out.println("=========DEBUG CREATE/ "+ user);
        Response response = keycloak.realm("carthago-realm").users().create(user);
        if(response.getStatus()==409){
            throw new RuntimeException("Utilisateur existant! Veuillez Modifier l'addresse Email ou Username" );

        }
        else {
            if (response.getStatus() != 201) {
                String body = response.readEntity(String.class); // le corps de la réponse JSON
                throw new RuntimeException("Erreur création utilisateur Keycloak : HTTP "
                        + response.getStatus() + " - " + body);
            }
        }

        String userId = getCreatedId(response);
        System.out.println("DEBUG +++++++++++ CREATED ID "+userId);
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
        try {
            System.out.println("Roles disponibles dans le realm :");
            keycloak.realm(realm).roles().list()
                    .forEach(r -> System.out.println(r.getName()));

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
            if(roleName.equals("admin")) {
                RoleRepresentation manageUsersRole = keycloak.realm(realm)
                        .clients()
                        .get(clientId)
                        .roles()
                        .get("manage-users")
                        .toRepresentation();

                RoleRepresentation manageRealmRole = keycloak.realm(realm)
                        .clients()
                        .get(clientId)
                        .roles()
                        .get("manage-realm")
                        .toRepresentation();
                keycloak.realm(realm).users().get(keycloakId).roles().clientLevel(clientId).add(List.of(manageUsersRole, manageRealmRole));
            }


        }catch(Exception e){
            e.printStackTrace();
        }

    }

    public List<UserRepresentation> getAllUsers() {
        return keycloak.realm("carthago-realm").users().list();
    }

    public void deleteUserById(String keycloakId) {
        try {
            keycloak.realm(realm).users().get(keycloakId).remove();
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la suppression dans Keycloak : " + e.getMessage());
        }
    }


    public UserRepresentation getUserById(String keycloakId) {
        try {
            return keycloak.realm(realm).users().get(keycloakId).toRepresentation();
        } catch (Exception e) {
            System.err.println("Erreur récupération user Keycloak : " + e.getMessage());
            return null;
        }
    }

    public void updateUser(String id, String firstName, String lastName, String email) {
        if (id == null || id.isEmpty()) {
            throw new IllegalArgumentException("❌ keycloakId est null ou vide !");
        }

        UserRepresentation user = keycloak.realm(realm)
                .users()
                .get(id)
                .toRepresentation();

        // Ne modifie PAS username ici
        // user.setUsername(username);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setEmail(email);

        keycloak.realm(realm).users().get(id).update(user);
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


    public void changeUserPassword(String userId, String newPassword) {
        UserResource userResource = keycloak.realm(realm).users().get(userId);

        CredentialRepresentation cred = new CredentialRepresentation();
        cred.setType(CredentialRepresentation.PASSWORD);
        cred.setValue(newPassword);
        cred.setTemporary(false);

        userResource.resetPassword(cred);
    }
    public String getUserIdByUsername(String username) {
        return keycloak.realm(realm).users().search(username).stream()
                .filter(u -> u.getUsername().equals(username))
                .findFirst()
                .map(UserRepresentation::getId)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
    }
    public void sendResetPasswordEmail(String email){
        try {
            UsersResource usersResource = keycloak.realm(realm).users();
            List<UserRepresentation> users = usersResource.search(null, null, null, email, 0, 1);
            for (UserRepresentation u : users) {
                System.out.println("User: " + u.getUsername() + " | " + u.getEmail());
            }
            if (users.isEmpty()) {
                throw new RuntimeException("Utilisateur non trouvé !");
            }

            UserResource user = usersResource.get(users.get(0).getId());
            List<String> actions = Collections.singletonList("UPDATE_PASSWORD");
            Integer lifespan = 3600;
            String finalRedirectUrl = "http://localhost:4200/account/login";
            user.executeActionsEmail("carthago-client-angular",finalRedirectUrl,actions);
            // Keycloak envoie le mail
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
    }
    public void resetPassword(String userId, String newPassword) {
        UserResource userResource = keycloak.realm(realm).users().get(userId);
        CredentialRepresentation credential = new CredentialRepresentation();
        credential.setType(CredentialRepresentation.PASSWORD);
        credential.setValue(newPassword);
        credential.setTemporary(false); // false = mot de passe permanent
        userResource.resetPassword(credential);
    }
}






