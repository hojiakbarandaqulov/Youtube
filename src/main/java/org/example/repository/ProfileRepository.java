package org.example.repository;

import jakarta.transaction.Transactional;
import org.example.entity.profile.ProfileEntity;
import org.example.enums.ProfileStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ProfileRepository extends JpaRepository<ProfileEntity, Long> {
   Optional<ProfileEntity> findByEmailAndVisibleTrue(String email);

   @Transactional
   @Modifying
   @Query("update ProfileEntity set status =?2 where id =?1")
   void updateStatus(Long profileId, ProfileStatus status);

   @Transactional
   @Modifying
   @Query("update ProfileEntity set email =?2 where id =?1")
   void updateEmail(Integer profileId, String email);
   boolean existsByEmail(String email);
}
