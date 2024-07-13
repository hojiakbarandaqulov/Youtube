package org.example.service;


import lombok.RequiredArgsConstructor;
import org.example.dto.AttachDTO;
import org.example.dto.channel.ChannelDTO;
import org.example.dto.playlist.PlayListCreateDto;
import org.example.dto.playlist.PlayListDto;
import org.example.dto.playlist.PlayListUpdateDto;
import org.example.dto.profile.ProfileDTO;
import org.example.entity.PlayListEntity;
import org.example.entity.profile.ProfileEntity;
import org.example.enums.PlayListStatus;
import org.example.enums.ProfileRole;
import org.example.exp.AppBadException;
import org.example.repository.PlaylistRepository;
import org.example.utils.SecurityUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class PlayListService {
    private final PlaylistRepository playlistRepository;
    private final AttachService attachService;

    public PlayListDto create(PlayListCreateDto dto) {
        PlayListEntity entity = toEntity(dto);
        PlayListEntity saved = playlistRepository.save(entity);
        return toDto(saved);
    }

    public PlayListDto update(Long playlistId, PlayListUpdateDto dto) {
        isOwner(playlistId);

        PlayListEntity playListEntity = getById(playlistId);
        if (dto.getName() != null) {
            playListEntity.setName(dto.getName());
        }
        if (dto.getDescription() != null) {
            playListEntity.setDescription(dto.getDescription());
        }

        if (dto.getOrderNumber() != null) {
            playListEntity.setOrderNumber(dto.getOrderNumber());
        }
        PlayListEntity saved = playlistRepository.save(playListEntity);
        return toDto(saved);
    }

    public void updateStatus(Long playlistId, PlayListStatus status) {
        isAdminOrOwner(playlistId);
        playlistRepository.updateStatus(playlistId, status);
    }

    public void delete(Long playlistId) {
        isAdminOrOwner(playlistId);
        playlistRepository.deleteById(playlistId);
    }

    public Page<PlayListDto> getAll(int pageNumber, int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<PlayListEntity> entityPage = playlistRepository.findAllBy(pageable);
        List<PlayListDto> list = entityPage.getContent()
                .stream()
                .map(this::toFullInfo)
                .toList();
        long totalElements = entityPage.getTotalElements();
        return new PageImpl<>(list, pageable, totalElements);
    }

    public List<PlayListDto> getByUserId(Long userId) {
        return playlistRepository.getByUserId(userId)
                .stream()
                .map(this::toFullInfo)
                .toList();
    }

    public List<PlayListDto> getByCurrentUserId() {
        Long userId = Long.valueOf(SecurityUtil.getProfileId());
        return playlistRepository.getByUserId(userId)
                .stream()
                .map(this::toShortInfo)
                .toList();
    }

    public List<PlayListDto> getByChanelId(String chanelId) {
        return playlistRepository.findAllByChanelId(chanelId)
                .stream()
                .map(this::toShortInfo)
                .toList();
    }

    public PlayListEntity getByPlaylistId(Long playlistId) {
        return getById(playlistId);
    }

    private PlayListDto toShortInfo(PlayListEntity entity) {
        PlayListDto dto = new PlayListDto();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setCreatedDate(entity.getCreatedDate());
        dto.setVideoCount(playlistRepository.videoCount());
        ///////////// video list

        // create channel
        ChannelDTO chanel = new ChannelDTO();
        chanel.setId(entity.getChanelId());
        chanel.setName(entity.getName());
        dto.setChanel(chanel);
        return dto;
    }

    private PlayListDto toFullInfo(PlayListEntity entity) {
        PlayListDto dto = new PlayListDto();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setDescription(entity.getDescription());
        dto.setOrderNumber(entity.getOrderNumber());
        dto.setStatus(entity.getStatus());
        dto.setCreatedDate(entity.getCreatedDate());

        // create channel
        ChannelDTO chanel = new ChannelDTO();
        chanel.setId(entity.getChanelId());
        chanel.setName(entity.getName());
        // create chanel photo
        AttachDTO chanelAttach = attachService.getDTOWithURL(entity.getChanel().getPhotoId());
        chanel.setPhoto(chanelAttach);

        dto.setChanel(chanel);

        // create profile
        ProfileDTO profile = new ProfileDTO();
        profile.setId(Long.valueOf(entity.getChanel().getProfileId()));
        profile.setName(entity.getChanel().getProfile().getName());
        profile.setSurname(entity.getChanel().getProfile().getSurname());
        // create profile photo
        AttachDTO profileAttach = attachService.getDTOWithURL(entity.getChanel().getProfile().getPhotoId());
        profile.setAttach(profileAttach);

        dto.setProfile(profile);
        return dto;
    }

    private PlayListEntity toEntity(PlayListCreateDto dto) {
        PlayListEntity entity = new PlayListEntity();
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
        entity.setStatus(dto.getStatus());
        entity.setChanelId(dto.getChanelId());
        entity.setOrderNumber(dto.getOrderNumber());
        return entity;
    }

    private PlayListDto toDto(PlayListEntity entity) {
        PlayListDto dto = new PlayListDto();
        dto.setName(entity.getName());
        dto.setDescription(entity.getDescription());
        dto.setStatus(entity.getStatus());
        dto.setChanelId(entity.getChanelId());
        dto.setOrderNumber(entity.getOrderNumber());
        dto.setCreatedDate(entity.getCreatedDate());
        return dto;
    }

    private void isAdminOrOwner(Long playlistId) {
        ProfileEntity profile = SecurityUtil.getProfile();
        PlayListEntity playListEntity = getById(playlistId);
        if (playListEntity.getChanel().getProfileId() != profile.getId()
                || !profile.getRole().equals(ProfileRole.ROLE_ADMIN)) {
            throw new AppBadException("You dont have permission to delete this playlist");
        }
    }

    private void isOwner(Long playlistId) {
        Integer profileId = SecurityUtil.getProfileId();
        PlayListEntity playListEntity = getById(playlistId);
        if (playListEntity.getChanel().getProfileId() != profileId) {
            throw new AppBadException("You dont have permission to update this playlist");
        }
    }

    public PlayListEntity getById(Long playlistId) {
        return playlistRepository.findById(playlistId)
                .orElseThrow(() -> new AppBadException("Playlist not found"));
    }
}
