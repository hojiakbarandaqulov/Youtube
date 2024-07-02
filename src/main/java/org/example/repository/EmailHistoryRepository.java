package org.example.repository;

import org.example.entity.history.EmailHistoryEntity;
import org.example.entity.history.EmailHistoryEntity;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.Optional;

public interface EmailHistoryRepository extends JpaRepository<EmailHistoryEntity, Integer> {

    Long countByEmailAndCreatedDateBetween(String email, LocalDateTime from, LocalDateTime to);

    // select count(*) from email_history createdDate between :from and :to
//    @Query("select e from EmailHistoryEntity e where e.email=:email order by e.createdDate DESC ")
    Optional<EmailHistoryEntity> findTopByEmailOrderByCreatedDateDesc(/*@Param("email")*/ String email);

    Optional<EmailHistoryEntity> findByCreatedDate(LocalDateTime createdDate);

    @NotNull
    Page<EmailHistoryEntity> findAll(@NotNull Pageable pageable);
}
