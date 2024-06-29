package org.example.repository;

import org.example.entity.history.EmailHistoryEntity;
import org.example.entity.history.EmailHistoryEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.Optional;

public interface EmailHistoryRepository extends JpaRepository<EmailHistoryEntity, Integer> {

    Long countByEmailAndCreatedDateBetween(String email, LocalDateTime from, LocalDateTime to);

//     select count(*) from email_history createdDate between :from and :to
    @Query(value = "select * from email_history order by created_date desc ",nativeQuery = true)
    Optional<EmailHistoryEntity> findByEmail(String email);


    Page<EmailHistoryEntity> findAll(Pageable pageable);
}
