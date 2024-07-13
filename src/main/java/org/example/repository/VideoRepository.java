package org.example.repository;

import org.example.entity.VideoEntity;
import org.example.mapper.VideoShortInfoMapper;
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


    List<VideoShortInfoMapper> findAllByTitle(String title);

    List<VideoShortInfoMapper> findAllByTagsId(Integer id);
}
