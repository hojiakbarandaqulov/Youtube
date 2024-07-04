package org.example.controller;

import io.jsonwebtoken.Header;
import org.example.dto.video.VideoDTO;
import org.example.dto.video.VideoUpdateDTO;
import org.example.enums.VideoStatus;
import org.example.service.VideoService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/video")
public class VideoController {
    private final VideoService videoService;

    public VideoController(VideoService videoService) {
        this.videoService = videoService;
    }

    @PreAuthorize("hasRole('USER')")
    @PostMapping("/create/video")
    public ResponseEntity<VideoDTO> createVideo(@RequestBody VideoDTO videoDTO) {
        VideoDTO videoDTO1 = videoService.create(videoDTO);
        return ResponseEntity.ok().body(videoDTO1);
    }

    @PreAuthorize("hasRole('USER')")
    @PutMapping("/update/video/{id}")
    public ResponseEntity<Boolean> updateVideo(@RequestBody VideoUpdateDTO videoDTO,
                                                      @PathVariable("id") String id) {
         videoService.update(videoDTO, id);
        return ResponseEntity.ok().body(true);
    }

    @PreAuthorize("hasRole('USER')")
    @PutMapping("/status/{id}")
    public ResponseEntity<VideoStatus> updateVideoStatus(@PathVariable("id") String id) {
        VideoStatus videoStatus = videoService.changeByStatus(id);
        return ResponseEntity.ok().body(videoStatus);
    }

    @PreAuthorize("hasRole('USER')")
    @PutMapping("/view_count/{id}")
    public ResponseEntity<String> viewCount(@PathVariable("id") String id) {
        videoService.increaseViewCount(id);
        return ResponseEntity.ok().body("count increased by one");
    }
}
