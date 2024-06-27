package org.example.repository;

import org.example.entity.ProfileEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProfileRepository extends JpaRepository<ProfileEntity, Long> {
   Optional<ProfileEntity> findByEmailAndVisibleTrue(String email);
}
