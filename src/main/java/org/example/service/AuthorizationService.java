package org.example.service;

import lombok.extern.slf4j.Slf4j;
import org.example.dto.AuthorizationResponseDTO;
import org.example.dto.LoginDTO;
import org.example.dto.RegistrationDTO;
import org.example.entity.profile.ProfileEntity;
import org.example.enums.LanguageEnum;
import org.example.enums.ProfileRole;
import org.example.enums.ProfileStatus;
import org.example.exp.AppBadException;
import org.example.repository.EmailHistoryRepository;
import org.example.repository.ProfileRepository;
import org.example.service.history.EmailHistoryService;
import org.example.utils.JwtUtil;
import org.example.utils.MD5Util;
import org.example.utils.RandomUtil;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Locale;
import java.util.Optional;

@Slf4j
@Service
public class AuthorizationService {

    private final ProfileRepository profileRepository;
    private final MailSenderService mailSenderService;
    private final EmailHistoryService emailHistoryService;
    private final ResourceBundleMessageSource resourceBundleMessageSource;

    public AuthorizationService(ProfileRepository profileRepository, MailSenderService mailSenderService, EmailHistoryService emailHistoryService, EmailHistoryRepository emailHistoryRepository, ResourceBundleMessageSource resourceBundleMessageSource, ResourceBundleMessageSource rbms) {
        this.profileRepository = profileRepository;
        this.mailSenderService = mailSenderService;
        this.emailHistoryService = emailHistoryService;
        this.resourceBundleMessageSource = resourceBundleMessageSource;
    }

    public String registration(RegistrationDTO dto, LanguageEnum language) {
        Optional<ProfileEntity> optional = profileRepository.findByEmailAndVisibleTrue(dto.getEmail());
        if (optional.isPresent()) {
            log.warn("Email already exists email => {}", dto.getEmail());
            String message = resourceBundleMessageSource.getMessage("email.exists", null, new Locale(language.name()));
            throw new AppBadException(message);
        }
        ProfileEntity entity = new ProfileEntity();
        entity.setName(dto.getName());
        entity.setSurname(dto.getSurname());
        entity.setEmail(dto.getEmail());
        entity.setPassword(MD5Util.getMD5(dto.getPassword()));
        entity.setCreatedDate(LocalDateTime.now());
        entity.setRole(ProfileRole.ROLE_USER);
        entity.setStatus(ProfileStatus.REGISTRATION);
        profileRepository.save(entity);
        // send email
        sendRegistrationEmail(entity.getId(), entity.getEmail());
        return resourceBundleMessageSource.getMessage("email.registration.verify", null, new Locale(language.name()));
    }

    public String authorizationVerification(Integer userId, LanguageEnum language) {
        Optional<ProfileEntity> optional = profileRepository.findById(userId);
        if (optional.isEmpty()) {
            log.warn("User not found => {}", userId);
            String message = resourceBundleMessageSource.getMessage("user.not.found", null, new Locale(language.name()));
            throw new AppBadException(message);
        }

        ProfileEntity entity = optional.get();
        if (!entity.getVisible() || !entity.getStatus().equals(ProfileStatus.REGISTRATION)) {
            String message = resourceBundleMessageSource.getMessage("registration.not.completed", null, new Locale(language.name()));
            throw new AppBadException(message);
        }

        profileRepository.updateStatus(userId, ProfileStatus.ACTIVE);
        return resourceBundleMessageSource.getMessage("Success", null, new Locale(language.name()));
    }

    //login email
    public AuthorizationResponseDTO login(LoginDTO dto, LanguageEnum language) {
        Optional<ProfileEntity> optional = profileRepository.findByEmailAndVisibleTrue(
                dto.getEmail());
        if (optional.isEmpty()) {
            String message = resourceBundleMessageSource.getMessage("item.not.found", null, new Locale(language.name()));
            throw new AppBadException(message);
        }

        ProfileEntity entity = optional.get();
        if (!entity.getPassword().equals(MD5Util.getMD5(dto.getPassword()))) {
            String message = resourceBundleMessageSource.getMessage("password.wrong", null, new Locale(language.name()));
            throw new AppBadException(message);
        }

        if (!entity.getStatus().equals(ProfileStatus.ACTIVE)) {
            String message = resourceBundleMessageSource.getMessage("status.wrong", null, new Locale(language.name()));
            throw new AppBadException(message);
        }
        AuthorizationResponseDTO responseDTO = new AuthorizationResponseDTO();
        responseDTO.setId(entity.getId());
        responseDTO.setName(entity.getName());
        responseDTO.setSurname(entity.getSurname());
        responseDTO.setRole(entity.getRole());
        responseDTO.setJwt(JwtUtil.encode(responseDTO.getId(), entity.getEmail(), responseDTO.getRole()));
        return responseDTO;
    }

    //email resend
    public String registrationResendEmail(String email, LanguageEnum language) {
        Optional<ProfileEntity> optional = profileRepository.findByEmailAndVisibleTrue(email);
        if (optional.isEmpty()) {
            String message = resourceBundleMessageSource.getMessage("email.not.exists", null, new Locale(language.name()));
            throw new AppBadException(message);
        }
        ProfileEntity entity = optional.get();
        emailHistoryService.isNotExpiredEmail(entity.getEmail()); // check for expireation date
        if (!entity.getVisible() || !entity.getStatus().equals(ProfileStatus.REGISTRATION)) {
            String message = resourceBundleMessageSource.getMessage("registration.not.completed", null, new Locale(language.name()));
            throw new AppBadException(message);
        }

        emailHistoryService.checkEmailLimit(email);
        sendRegistrationRandomCodeEmail(entity.getId(), email);
        String message = resourceBundleMessageSource.getMessage("email.registration.verify", null, new Locale(language.name()));
        return message;
    }

    public void sendRegistrationRandomCodeEmail(Integer profileId, String email) {
        // send email
        String url = "http://localhost:8080/auth/verification/" + profileId;
        String text = String.format(RandomUtil.getRandomSmsCode(), url);
        mailSenderService.send(email, "Complete registration", text);
        emailHistoryService.crete(email, text); // create history
    }

    public void sendRegistrationEmail(Integer profileId, String email) {
        // send email
        String url = "http://localhost:8080/auth/verification/" + profileId;
        String formatText = "<style>\n" +
                "    a:link, a:visited {\n" +
                "        background-color: #f44336;\n" +
                "        color: white;\n" +
                "        padding: 14px 25px;\n" +
                "        text-align: center;\n" +
                "        text-decoration: none;\n" +
                "        display: inline-block;\n" +
                "    }\n" +
                "\n" +
                "    a:hover, a:active {\n" +
                "        background-color: red;\n" +
                "    }\n" +
                "</style>\n" +
                "<div style=\"text-align: center\">\n" +
                "    <h1>Welcome to Youtube web portal</h1>\n" +
                "    <br>\n" +
                "    <p>Please button lick below to complete registration</p>\n" +
                "    <div style=\"text-align: center\">\n" +
                "        <a href=\"%s\" target=\"_blank\">This is a link</a>\n" +
                "    </div>";
        String text = String.format(formatText, url);
        mailSenderService.send(email, "Complete registration", text);
        emailHistoryService.crete(email, text); // create history
    }
}
