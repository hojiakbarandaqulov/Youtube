package org.example.controller;

import lombok.extern.slf4j.Slf4j;
import org.example.dto.channel.ChannelDTO;
import org.example.service.ChannelService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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
}
