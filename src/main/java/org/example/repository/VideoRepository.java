package org.example.repository;

import org.example.entity.VideoEntity;
import org.example.mapper.VideoShortInfoMapper;
import org.springdoc.core.converters.models.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface VideoRepository extends JpaRepository<VideoEntity, String> {

    @Transactional
    @Modifying
    @Query("UPDATE VideoEntity set viewCount = COALESCE(viewCount,0)+1 where id=?1")
    void increaseViewCount(String id);

    Optional<VideoShortInfoMapper> getVideoList(PageRequest pageable);
}
