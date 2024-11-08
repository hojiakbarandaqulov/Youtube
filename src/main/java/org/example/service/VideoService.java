package org.example.service;

import lombok.extern.slf4j.Slf4j;
import org.example.dto.video.VideoDTO;
import org.example.dto.video.VideoUpdateDTO;
import org.example.entity.VideoEntity;
import org.example.enums.VideoStatus;
import org.example.exp.AppBadException;
import org.example.mapper.VideoShortInfoMapper;
import org.example.repository.VideoRepository;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class VideoService {

    private final VideoRepository videoRepository;

    public VideoService(VideoRepository videoRepository) {
        this.videoRepository = videoRepository;
    }


    public VideoDTO create(VideoDTO videoDTO) {
        VideoEntity entity = new VideoEntity();
        entity.setTitle(videoDTO.getTitle());
        entity.setDescription(videoDTO.getDescription());
        entity.setChannelId(videoDTO.getChannelId());
        entity.setType(videoDTO.getType());
        entity.setStatus(VideoStatus.PRIVATE);
        entity.setAttachId(videoDTO.getAttachId());
        entity.setCategoryId(videoDTO.getCategoryId());
        videoRepository.save(entity);
        return videoCreateToDTO(entity);
    }

    public VideoDTO videoCreateToDTO(VideoEntity entity) {
        VideoDTO videoResponse = new VideoDTO();
        videoResponse.setTitle(entity.getTitle());
        videoResponse.setDescription(entity.getDescription());
        videoResponse.setChannelId(entity.getChannelId());
        videoResponse.setType(entity.getType());
        videoResponse.setStatus(entity.getStatus());
        videoResponse.setAttachId(entity.getAttachId());
        videoResponse.setCategoryId(entity.getCategoryId());
        return videoResponse;
    }

    public void update(VideoUpdateDTO videoDTO, String id) {
        VideoEntity entity = get(id);
        entity.setTitle(videoDTO.getTitle());
        entity.setDescription(videoDTO.getDescription());
        entity.setCategoryId(videoDTO.getCategoryId());
        entity.setType(videoDTO.getType());
        entity.setStatus(videoDTO.getStatus());
        entity.setAttachId(videoDTO.getAttachId());
        videoRepository.save(entity);
    }

    public VideoEntity get(String id) {
        return videoRepository.findById(id).orElseThrow(() -> {
            log.error("Video not found id = {}", id);
            throw new AppBadException("Video not found");
        });
    }

    public VideoStatus changeByStatus(String id) {
        VideoEntity videoEntity = get(id);
        if (videoEntity.getStatus() == VideoStatus.PRIVATE) {
            videoEntity.setStatus(VideoStatus.PUBLIC);
        } else if (videoEntity.getStatus() == VideoStatus.PUBLIC) {
            videoEntity.setStatus(VideoStatus.PRIVATE);
        }
        VideoEntity save = videoRepository.save(videoEntity);
        return save.getStatus();
    }

    public void increaseViewCount(String id) {
        videoRepository.increaseViewCount(id);
    }

    public PageImpl<VideoDTO> pagination(Integer page, Integer size) {
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by("createdDate").descending());
        Page<VideoEntity> PageObj = videoRepository.findAll(pageRequest);
        List<VideoDTO> videoList = new LinkedList<>();
        for (VideoEntity entity : PageObj.getContent()) {
            VideoDTO videoDTO = videoCreateToDTO(entity);
            videoList.add(videoDTO);
        }
        Long total = PageObj.getTotalElements();
        return new PageImpl<VideoDTO>(videoList, pageRequest, total);
    }

    public List<VideoDTO> videoByTitle(String title) {
        List<VideoShortInfoMapper> titleList = videoRepository.findAllByTitle(title);
        return getVideoDTOS(titleList);
    }

    public List<VideoDTO> tagPagination(Integer id) {
        List<VideoShortInfoMapper> tagId = videoRepository.findAllByTagsId(id);
        return getVideoDTOS(tagId);
    }

    @NotNull
    private List<VideoDTO> getVideoDTOS(List<VideoShortInfoMapper> tagId) {
        List<VideoDTO> videoDTOList = new LinkedList<>();
        for (VideoShortInfoMapper entity : tagId) {
            VideoDTO videoDTO=new VideoDTO();
            videoDTO.setAttachId(entity.getPreviewAttachId());
            videoDTO.setTitle(entity.getTitle());
            videoDTO.setChannelId(entity.getChannelId());
            videoDTO.setDescription(entity.getDescription());
            videoDTO.setStatus(entity.getStatus());
            videoDTO.setType(entity.getType());
            videoDTO.setAttachId(entity.getAttachId());
            videoDTO.setCategoryId(entity.getCategoryId());
            videoDTOList.add(videoDTO);
        }
        return videoDTOList;
    }
}
