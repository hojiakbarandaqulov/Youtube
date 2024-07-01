package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.controller.AuthorizationController;
import org.example.dto.auth.JwtDTO;
import org.example.dto.profile.ProfileChangePasswordDTO;
import org.example.dto.profile.ProfileCreateDTO;
import org.example.dto.profile.ProfileDTO;
import org.example.dto.profile.ProfileUpdateDTO;
import org.example.entity.profile.ProfileEntity;
import org.example.enums.ProfileStatus;
import org.example.exp.AppBadException;
import org.example.repository.ProfileRepository;
import org.example.utils.JwtUtil;
import org.example.utils.MD5Util;
import org.example.utils.SecurityUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProfileService {
    private final ProfileRepository profileRepository;
    private final EmailSenderService emailSenderService;
    private final EmailHistoryService emailHistoryService;
    private static final Logger logger = LoggerFactory.getLogger(AuthorizationController.class);




    public String changePassword(ProfileChangePasswordDTO dto) {
        ProfileEntity profile = SecurityUtil.getProfile();
        if (!profile.getPassword().equals(MD5Util.getMD5(dto.getOldPassword()))) {
            throw new AppBadException("wrong password");
        }
        profile.setPassword(MD5Util.getMD5(dto.getNewPassword()));
        profileRepository.save(profile);
        return "password changed";
    }

    public String changeEmail(String newEmail) {
        ProfileEntity profile = SecurityUtil.getProfile();

        if (profile.getStatus().equals(ProfileStatus.BLOCK)) {
            throw new AppBadException("profile status is block");
        }
        if (profile.getEmail().equals(newEmail) || profileRepository.existsByEmail(newEmail)) {
            throw new AppBadException("email already in use");
        }

        String token = JwtUtil.generateToken(Long.valueOf(profile.getId()), newEmail, profile.getRole());
        emailSenderService.sendEmailForChange(token);
        return "please verify your new email.";
    }

    public String verifyEmail(String token) {
        JwtDTO dto = JwtUtil.decode(token);
        String email = dto.getUsername();
        Integer profileId = dto.getId();

        emailHistoryService.isNotExpiredEmail(email);

        profileRepository.updateEmail(profileId, email);
        return "email changed successfully";
    }

    public ProfileDTO update(ProfileUpdateDTO dto) {
        ProfileEntity profile = SecurityUtil.getProfile();

        if (profile.getStatus().equals(ProfileStatus.BLOCK)) {
            throw new AppBadException("profile status is block");
        }

        profile.setName(dto.getName());
        profile.setSurname(dto.getSurname());
        ProfileEntity saved = profileRepository.save(profile);
        return toDTO(saved);
    }

    public ProfileDTO getProfileDetail() {
        ProfileEntity profile = SecurityUtil.getProfile();
        return toDTO(profile);
    }
    public ProfileDTO create(ProfileCreateDTO dto) {
        profileRepository.findByEmailAndVisibleTrue(dto.getEmail())
                .ifPresent(profile -> {
                    throw new AppBadException("profile already exists");
                });
        ProfileEntity entity = new ProfileEntity();

        entity.setName(dto.getName());
        entity.setEmail(dto.getEmail());
        entity.setStatus(ProfileStatus.ACTIVE);
        entity.setRole(dto.getRole());
        entity.setPassword("12345");
        entity.setSurname(dto.getSurname());
        profileRepository.save(entity);
        return toDTO(entity);
    }

    public ProfileEntity get(Long id) {
        return profileRepository.findById(id).orElseThrow(() -> {
            logger.error("Profile not found id = {}", id);
            throw new AppBadException("Profile not found");
        });
    }

   /* private ProfileEntity toEntity(ProfileCreateDTO dto) {
        ProfileEntity entity = new ProfileEntity();
        entity.setName(dto.getName());
        entity.setSurname(dto.getSurname());
        entity.setEmail(dto.getEmail());
        entity.setRole(dto.getRole());
        entity.setStatus(ProfileStatus.ACTIVE);
        return entity;
    }*/

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
}
