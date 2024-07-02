package org.example.controller;

import lombok.extern.slf4j.Slf4j;
import org.example.dto.channel.*;
import org.example.service.ChannelService;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/channel")
public class ChannelController {
    private final ChannelService channelService;

    public ChannelController(ChannelService channelService) {
        this.channelService = channelService;
    }

    @PreAuthorize("hasRole('USER')")
    @PostMapping("/create")
    public ResponseEntity<ChannelDTO> createChannel(@RequestBody ChannelDTO channelDTO) {
        ChannelDTO channel = channelService.createChannel(channelDTO);
        log.info("channel created: {}", channel);
        return ResponseEntity.ok().body(channel);
    }

    @PreAuthorize("hasRole('USER')")
    @PutMapping("/update/{id}")
    public ResponseEntity<Boolean> updateChannel(@RequestBody ChannelDTO channelDTO,
                                                    @PathVariable("id") String id) {
        ChannelDTO channel = channelService.updateChannel(channelDTO, id);
        log.info("channel update: {}", channel);
        return ResponseEntity.ok().body(true);
    }

    @PreAuthorize("hasRole('USER')")
    @PutMapping("/updatePhoto/{id}")
    public ResponseEntity<Boolean> updatePhotoChannel(@RequestBody ChannelUpdatePhotoDTO channelDTO,
                                                                    @PathVariable("id") String id) {
        ChannelUpdatePhotoDTO channel = channelService.updatePhotoChannel(channelDTO,id);
        log.info("channel updatePhoto: {}", channel);
        return ResponseEntity.ok().body(true);
    }

    @PreAuthorize("hasRole('USER')")
    @PutMapping("/updateBanner/{id}")
    public ResponseEntity<Boolean> updateBannerChannel(@RequestBody ChannelUpdateBannerDTO channelDTO,
                                                       @PathVariable("id") String id) {
        ChannelUpdateBannerDTO channel = channelService.updateBannerChannel(channelDTO,id);
        log.info("channel updateBanner: {}", channel);
        return ResponseEntity.ok().body(true);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/pagination")
    public ResponseEntity<PageImpl<ChannelDTO>> pagination(@RequestParam(value = "page", defaultValue = "1") Integer page,
                                                           @RequestParam(value = "size", defaultValue = "10") Integer size) {
        PageImpl<ChannelDTO> response = channelService.pagination(page-1, size);
        log.info("channel pagination: {}", response);
        return ResponseEntity.ok().body(response);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/getById/{id}")
    public ResponseEntity<ChannelDTO> getById(@PathVariable("id") String id) {
        ChannelDTO channel = channelService.getById(id);
        log.info("channel getById: {}", id);
        return ResponseEntity.ok().body(channel);
    }

    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    @PutMapping("/changeStatus/{id}")
    public ResponseEntity<ChannelStatusDTO>changeStatus(@PathVariable("id") String id){
        ChannelStatusDTO channelStatusDTO = channelService.changeStatus(id);
        log.info("channel change status: {}",id);
        return ResponseEntity.ok().body(channelStatusDTO);
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/getChannelList/{profileId}/{channelId}")
    public ResponseEntity<ChannelDTO> getById(@PathVariable("channelId") String channelId,
                                              @PathVariable("profileId") Integer profileId) {
        ChannelDTO channel = channelService.getBuUserChannelList(channelId,profileId);
        return ResponseEntity.ok().body(channel);
    }
}
