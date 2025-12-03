package com.attijaristage.abtauth.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.http.HttpMethod; // Import nécessaire pour HttpMethod

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Configuration
@EnableMethodSecurity(jsr250Enabled = true, prePostEnabled = true)
public class SecurityConfig {
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .cors(Customizer.withDefaults())
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        // FIX CRITIQUE: Permet explicitement la requête OPTIONS (CORS preflight)
                        // Ces requêtes ne doivent jamais être sécurisées.
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

                        // ENDPOINTS PUBLICS
                        .requestMatchers("/api/user-profiles/login").permitAll()
                        .requestMatchers("/api/user-profiles/reset-password").permitAll()
                        .requestMatchers( "/api/user-profiles/forgot-password").permitAll()

                        // ENDPOINTS SECURISES
                        .requestMatchers("/api/user-profiles/profile").authenticated()
                        .anyRequest().authenticated()
                )
                // Configure l'utilisation des jetons JWT pour les ressources sécurisées
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter()))
                );


        return http.build();
    }

    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
        converter.setJwtGrantedAuthoritiesConverter(jwt -> {
            // Logique pour extraire les rôles de Keycloak
            List<String> roles = jwt.getClaimAsMap("realm_access") != null ?
                    (List<String>) ((Map<String, Object>) jwt.getClaim("realm_access")).get("roles") :
                    List.of();

            return roles.stream()
                    .map(roleName -> new SimpleGrantedAuthority("ROLE_" + roleName))
                    .collect(Collectors.toList());
        });
        return converter;
    }
}
