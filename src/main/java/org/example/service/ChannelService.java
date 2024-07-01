package org.example.service;

import org.example.dto.channel.ChannelDTO;
import org.example.entity.ChannelEntity;
import org.example.enums.ChannelStatus;
import org.example.repository.ChannelRepository;
import org.springframework.stereotype.Service;

@Service
public class ChannelService {
    private final ChannelRepository channelRepository;

    public ChannelService(ChannelRepository channelRepository) {
        this.channelRepository = channelRepository;
    }

    public ChannelDTO createChannel(ChannelDTO channelDTO) {
        ChannelEntity entity=new ChannelEntity();
        entity.setName(channelDTO.getName());
        entity.setPhoto(channelDTO.getPhoto());
        entity.setDescription(channelDTO.getDescription());
        entity.setStatus(ChannelStatus.ACTIVE);
        entity.setBanner(channelDTO.getBanner());
        channelRepository.save(entity);
        return channelToDTO(entity);
    }

    public ChannelDTO channelToDTO(ChannelEntity channelEntity) {
        ChannelDTO newChannelDTO=new ChannelDTO();
        newChannelDTO.setName(channelEntity.getName());
        newChannelDTO.setPhoto(channelEntity.getPhoto());
        newChannelDTO.setDescription(channelEntity.getDescription());
        newChannelDTO.setStatus(channelEntity.getStatus());
        newChannelDTO.setBanner(channelEntity.getBanner());
        return newChannelDTO;
    }
}
