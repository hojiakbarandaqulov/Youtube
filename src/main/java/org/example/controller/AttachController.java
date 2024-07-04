package org.example.controller;

import lombok.extern.slf4j.Slf4j;
import org.example.dto.AttachDTO;
import org.example.service.AttachService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
@Slf4j
@RestController
@RequestMapping("/attach")
public class AttachController {
    private final AttachService attachService;

    @Autowired
    public AttachController(AttachService attachService) {
        this.attachService = attachService;
    }

    @PostMapping("/upload")
    public ResponseEntity<AttachDTO> upload(@RequestParam("file") MultipartFile file) {
        AttachDTO attachDTO = attachService.saveAttach(file);
        return ResponseEntity.ok().body(attachDTO);
    }

    @GetMapping(value = "/open/{fileName}", produces = MediaType.IMAGE_PNG_VALUE)
    public byte[] open(@PathVariable("fileName") String fileName) {
        return attachService.loadImage(fileName);
    }

    @GetMapping("/download/{fileName}")
    public ResponseEntity download(@PathVariable("fileName") String fileName) {
        return attachService.download(fileName);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping(value = "/pagination")
    public ResponseEntity<PageImpl<AttachDTO>> pagination(@RequestParam(value = "page", defaultValue = "1") Integer page,
                                                          @RequestParam(value = "size", defaultValue = "10") Integer size) {
        PageImpl<AttachDTO> response = attachService.getAttachPagination(page - 1, size);
        return ResponseEntity.ok().body(response);

    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Boolean> delete(@PathVariable("id") String id) {
        Boolean delete = attachService.delete(id);
        return ResponseEntity.ok().body(delete);
    }
}
