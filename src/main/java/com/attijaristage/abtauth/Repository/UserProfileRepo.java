package com.attijaristage.abtauth.Repository;

import com.attijaristage.abtauth.Entities.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserProfileRepo extends JpaRepository<UserProfile, Long> {
    Optional<UserProfile> findByKeycloakId(String keycloakId);
}

