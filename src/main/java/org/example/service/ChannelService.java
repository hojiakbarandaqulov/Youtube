package org.example.service;

import lombok.extern.slf4j.Slf4j;
import org.example.dto.channel.*;
import org.example.entity.ChannelEntity;
import org.example.entity.profile.ProfileEntity;
import org.example.enums.ChannelStatus;
import org.example.exp.AppBadException;
import org.example.repository.ChannelRepository;
import org.example.utils.SecurityUtil;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class ChannelService {
    private final ChannelRepository channelRepository;

    public ChannelService(ChannelRepository channelRepository) {
        this.channelRepository = channelRepository;
    }

    public ChannelDTO createChannel(ChannelDTO channelDTO) {
        Optional<ChannelEntity> byName = channelRepository.findByName(channelDTO.getName());
        if (byName.isPresent()){
            throw new AppBadException("Such name channel there is channel you can't open");
        }
        ChannelEntity entity=new ChannelEntity();
        entity.setName(channelDTO.getName());
        entity.setPhotoId(channelDTO.getPhoto());
        entity.setDescription(channelDTO.getDescription());
        entity.setStatus(ChannelStatus.ACTIVE);
        entity.setBannerId(channelDTO.getBanner());
        entity.setProfileId(SecurityUtil.getProfileId());
        channelRepository.save(entity);
        return channelToDTO(entity);
    }

    public ChannelDTO channelToDTO(ChannelEntity channelEntity) {
        ChannelDTO newChannelDTO=new ChannelDTO();
        newChannelDTO.setName(channelEntity.getName());
        newChannelDTO.setPhoto(channelEntity.getPhotoId());
        newChannelDTO.setDescription(channelEntity.getDescription());
        newChannelDTO.setStatus(channelEntity.getStatus());
        newChannelDTO.setBanner(channelEntity.getBannerId());
        newChannelDTO.setProfileId(channelEntity.getProfileId());
        return newChannelDTO;
    }

    public ChannelDTO updateChannel(ChannelDTO channelDTO, String id) {
       ChannelEntity entity=get(id);
       entity.setName(channelDTO.getName());
       entity.setPhotoId(channelDTO.getPhoto());
       entity.setDescription(channelDTO.getDescription());
       entity.setStatus(channelDTO.getStatus());
       entity.setBannerId(channelDTO.getBanner());
       entity.setProfileId(channelDTO.getProfileId());
       channelRepository.save(entity);
       return channelToDTO(entity);
    }


    public ChannelEntity get(String id) {
        return channelRepository.findById(id).orElseThrow(() -> {
            log.error("Channel not found  = {}", id);
            throw new AppBadException("Channel not found");
        });
    }

    public ChannelUpdatePhotoDTO updatePhotoChannel(ChannelUpdatePhotoDTO channelDTO,
                                                    String id) {
        ChannelEntity entity=get(id);
        entity.setPhotoId(channelDTO.getPhoto());
        channelRepository.save(entity);
        ChannelUpdatePhotoDTO updatePhoto = new ChannelUpdatePhotoDTO();
        updatePhoto.setPhoto(channelDTO.getPhoto());
        return updatePhoto;
    }

    public ChannelUpdateBannerDTO updateBannerChannel(ChannelUpdateBannerDTO channelDTO,
                                                      String id) {
        ChannelEntity entity=get(id);
        entity.setBannerId(channelDTO.getBanner());
        channelRepository.save(entity);
        ChannelUpdateBannerDTO updateBanner = new ChannelUpdateBannerDTO();
        updateBanner.setBanner(channelDTO.getBanner());
        return updateBanner;
    }

    public PageImpl<ChannelDTO> pagination(Integer page, Integer size) {
        Sort sort= Sort.by("createdDate");
        Pageable pageable= PageRequest.of(page,size,sort);
        Page<ChannelEntity> pageObj=channelRepository.findAll(pageable);
        List<ChannelDTO> list=new LinkedList<>();
        for (ChannelEntity entity:pageObj.getContent()){
            list.add(channelToDTO(entity));
        }
        Long total=pageObj.getTotalElements();
        return new PageImpl<ChannelDTO>(list,pageable,total);
    }

    public ChannelDTO getById(String id) {
        ChannelEntity channelEntity=get(id);
        return channelToDTO(channelEntity);
    }

    public ChannelStatusDTO changeStatus(String id) {
        ChannelEntity entity=get(id);
        if (entity.getStatus().equals(ChannelStatus.ACTIVE)){
            entity.setStatus(ChannelStatus.BLOCK);
        }else if (entity.getStatus().equals(ChannelStatus.BLOCK)){
            entity.setStatus(ChannelStatus.ACTIVE);
        }
        channelRepository.save(entity);
        return  ChannelStatusToDTO(entity);
    }

    private ChannelStatusDTO ChannelStatusToDTO(ChannelEntity entity) {
        ChannelStatusDTO statusDTO=new ChannelStatusDTO();
        statusDTO.setStatus(entity.getStatus());
        return statusDTO;
    }

    public ChannelDTO getBuUserChannelList(String channelId, Integer profileId) {
        ChannelEntity channelEntity=get(channelId);
        if (!profileId.equals(channelEntity.getProfileId())){
            throw new AppBadException("profileId not match");
        }
        return getChannelListToDTO(channelEntity);
    }

    public ChannelDTO getChannelListToDTO(ChannelEntity entity){
        ChannelDTO channelDTO=new ChannelDTO();
        channelDTO.setName(entity.getName());
        channelDTO.setPhoto(entity.getPhotoId());
        channelDTO.setDescription(entity.getDescription());
        channelDTO.setStatus(entity.getStatus());
        channelDTO.setBanner(entity.getBannerId());
        channelDTO.setProfileId(entity.getProfileId());
        return channelDTO;
    }
}
