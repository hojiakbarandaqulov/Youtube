package org.example.repository;

import org.example.entity.history.EmailHistoryEntity;
import org.example.entity.history.EmailHistoryEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.Optional;

public interface EmailHistoryRepository extends JpaRepository<EmailHistoryEntity, Integer> {

    Long countByEmailAndCreatedDateBetween(String email, LocalDateTime from, LocalDateTime to);

    // select count(*) from email_history createdDate between :from and :to
    Optional<EmailHistoryEntity> findByEmail(String email);

    Optional<EmailHistoryEntity> findByCreatedDate(LocalDateTime createdDate);

    Page<EmailHistoryEntity> findAll(Pageable pageable);
}
