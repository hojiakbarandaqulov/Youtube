package org.example.service;

import org.example.dto.tag.TagAllDTO;
import org.example.dto.tag.TagDTO;
import org.example.dto.tag.TagUpdateDTO;
import org.example.entity.TagEntity;
import org.example.exp.AppBadException;
import org.example.repository.TagRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Service
public class TagService {
    private final TagRepository tagRepository;

    public TagService(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    public TagDTO create(TagDTO tagDTO) {
        Optional<TagEntity> optional = tagRepository.findByName(tagDTO.getName());
        if (optional.isPresent()) {
            throw new AppBadException("Tag already exists");
        }
        TagEntity tagEntity=new TagEntity();
        tagEntity.setName(tagDTO.getName());
        tagEntity.setCreatedDate(LocalDateTime.now());
        tagRepository.save(tagEntity);
        return toDTO(tagEntity);
    }

    public TagDTO toDTO(TagEntity tagEntity) {
        TagDTO tagDTO = new TagDTO();
        tagDTO.setName(tagEntity.getName());
        return tagDTO;
    }

    public Boolean update(TagUpdateDTO tagUpdateDTO) {
        Optional<TagEntity> optional = tagRepository.findById(tagUpdateDTO.getId());
        if (optional.isEmpty()){
            throw new AppBadException("Tag not found");
        }
        TagEntity tagEntity = optional.get();
        tagEntity.setName(tagUpdateDTO.getName());
        tagEntity.setCreatedDate(LocalDateTime.now());
        tagRepository.save(tagEntity);
        return true;
    }

    public Boolean delete(Integer id) {
        tagRepository.deleteById(id);
        return true;
    }

    public List<TagAllDTO> getAll() {
        List<TagEntity> all = tagRepository.findAll();
        List<TagAllDTO> tagAll = new LinkedList<>();
        for (TagEntity tagEntity : all) {
            tagAll.add(toAllDTO(tagEntity));
        }
        return tagAll;
    }

    public TagAllDTO toAllDTO(TagEntity tagEntity) {
        TagAllDTO tagAllDTO = new TagAllDTO();
        tagAllDTO.setId(tagEntity.getId());
        tagAllDTO.setName(tagEntity.getName());
        tagAllDTO.setCreatedDate(tagEntity.getCreatedDate());
        return tagAllDTO;
    }
}
