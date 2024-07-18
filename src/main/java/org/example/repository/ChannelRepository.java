package org.example.repository;

import org.example.entity.ChannelEntity;
import org.example.enums.ChannelStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ChannelRepository extends JpaRepository<ChannelEntity, String> {
    Optional<ChannelEntity> findByName(String name);

    Optional<ChannelEntity> findByStatus(ChannelStatus status);
}
