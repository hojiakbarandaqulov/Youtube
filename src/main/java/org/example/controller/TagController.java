package org.example.controller;

import org.example.dto.tag.TagAllDTO;
import org.example.dto.tag.TagDTO;
import org.example.dto.tag.TagUpdateDTO;
import org.example.service.TagService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tag")
public class TagController {
    private final TagService tagService;

    public TagController(TagService tagService) {
        this.tagService = tagService;
    }

    @PostMapping("/create/any")
    public ResponseEntity<TagDTO> create(@RequestBody TagDTO tagDTO) {
        TagDTO dto = tagService.create(tagDTO);
        return ResponseEntity.ok().body(dto);
    }

    @PostMapping("/update")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Boolean> update(@RequestBody TagUpdateDTO tagUpdateDTO) {
        Boolean update = tagService.update(tagUpdateDTO);
        return ResponseEntity.ok().body(update);
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Boolean> delete(@PathVariable Integer id) {
        Boolean delete = tagService.delete(id);
        return ResponseEntity.ok().body(delete);
    }

    @GetMapping("/any/all")
    public ResponseEntity<List<TagAllDTO>> getAll() {
        List<TagAllDTO> all = tagService.getAll();
        return ResponseEntity.ok().body(all);
    }


}
