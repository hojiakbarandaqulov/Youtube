package org.example.repository;

import org.example.entity.AttachEntity;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AttachRepository extends JpaRepository<AttachEntity, String> {


   @NotNull
   Optional<AttachEntity> findById(@NotNull String id);

}
