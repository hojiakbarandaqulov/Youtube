package org.example.service;

import lombok.extern.slf4j.Slf4j;
import org.example.dto.profile.ProfileCreateDTO;
import org.example.dto.profile.ProfileDTO;
import org.example.entity.profile.ProfileEntity;
import org.example.enums.ProfileStatus;
import org.example.exp.AppBadException;
import org.example.repository.ProfileRepository;
import org.springframework.stereotype.Service;
@Slf4j
@Service
public class ProfileService {
    private final ProfileRepository profileRepository;

    public ProfileService(ProfileRepository profileRepository) {
        this.profileRepository = profileRepository;
    }

  /*  public ProfileDTO create(ProfileCreateDTO dto) {
        ProfileEntity save = profileRepository.save(toEntity(dto));
        return toDTO(save);
    }

    public ProfileEntity get(Integer id) {
        return profileRepository.findById(id).orElseThrow(() -> {
            log.error("Profile not found id = {}", id);
            throw new AppBadException("Profile not found");
        });
    }*/

    private ProfileEntity toEntity(ProfileCreateDTO dto) {
        ProfileEntity entity = new ProfileEntity();
        entity.setName(dto.getName());
        entity.setSurname(dto.getSurname());
        entity.setEmail(dto.getEmail());
        entity.setRole(dto.getRole());
        entity.setStatus(ProfileStatus.ACTIVE);
        return entity;
    }

    private ProfileDTO toDTO(ProfileEntity entity) {
        ProfileDTO dto = new ProfileDTO();
        dto.setId(Long.valueOf(entity.getId()));
        dto.setName(entity.getName());
        dto.setSurname(entity.getSurname());
        dto.setEmail(entity.getEmail());
        dto.setRole(entity.getRole());
        dto.setStatus(entity.getStatus());
        dto.setCreatedDate(entity.getCreatedDate());
        return dto;
    }

 /*   public Boolean changePassword(ProfileChangePasswordDTO dto) {
        Integer profileId = SecurityUtil.getProfileId();
        ProfileEntity entity= get(profileId);
        if (!entity.getPassword().equals(MD5Util.getMD5(entity.getPassword()))){
           throw new AppBadException("Old password wrong");
        }
        entity.setPassword(dto.getNewPassword());
        profileRepository.save(entity);
        return true;
    }*/
}
