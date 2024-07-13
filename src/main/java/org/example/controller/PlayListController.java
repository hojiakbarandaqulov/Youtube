package org.example.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.dto.playlist.PlayListCreateDto;
import org.example.dto.playlist.PlayListDto;
import org.example.dto.playlist.PlayListUpdateDto;
import org.example.entity.PlayListEntity;
import org.example.enums.PlayListStatus;
import org.example.service.PlayListService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/playlist")
@RequiredArgsConstructor
public class PlayListController {
    private final PlayListService playListService;

    @PostMapping("/create")
    public ResponseEntity<PlayListDto> create(@Valid @RequestBody PlayListCreateDto dto) {
        PlayListDto response = playListService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/update/{playlistId}")
    public ResponseEntity<PlayListDto> update(@PathVariable Long playlistId,
                                              @Valid @RequestBody PlayListUpdateDto dto) {
        PlayListDto response = playListService.update(playlistId, dto);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PutMapping("/update/status/{playlistId}")
    public ResponseEntity<String> updateStatus(@PathVariable Long playlistId,
                                               @RequestParam PlayListStatus status) {
        playListService.updateStatus(playlistId, status);
        return ResponseEntity.status(HttpStatus.OK).body("playlist status updated");
    }

    @DeleteMapping("/delete/{playlistId}")
    public ResponseEntity<String> delete(@PathVariable Long playlistId) {
        playListService.delete(playlistId);
        return ResponseEntity.status(HttpStatus.OK).body("playlist deleted");
    }

    @GetMapping("/getAll")
    public ResponseEntity<Page<PlayListDto>> getAll(@RequestParam(defaultValue = "1") int pageNumber,
                                                    @RequestParam(defaultValue = "5") int pageSize) {
        Page<PlayListDto> response = playListService.getAll(pageNumber-1,pageSize);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/byUserId/{userId}")
    public ResponseEntity<List<PlayListDto>> getByUserId(@PathVariable Long userId) {
        List<PlayListDto> response = playListService.getByUserId(userId);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/currentUser")
    public ResponseEntity<List<PlayListDto>> getByCurrentUserId() {
        List<PlayListDto> response = playListService.getByCurrentUserId();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/byChanelId/{chanelId}")
    public ResponseEntity<List<PlayListDto>> getByChanelId(@PathVariable String chanelId) {
        List<PlayListDto> response = playListService.getByChanelId(chanelId);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/byId")  ////////  MAZGI BU CHALA QOLDI
    public ResponseEntity<PlayListEntity> getByPlaylistId(@RequestParam Long playlistId) {
        PlayListEntity response = playListService.getByPlaylistId(playlistId);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
