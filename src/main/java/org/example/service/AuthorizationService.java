package org.example.service;

import lombok.extern.slf4j.Slf4j;
import org.example.dto.AuthorizationResponseDTO;
import org.example.dto.LoginDTO;
import org.example.dto.RegistrationDTO;
import org.example.entity.profile.ProfileEntity;
import org.example.entity.history.EmailHistoryEntity;
import org.example.enums.EmailHistoryStatus;
import org.example.enums.ProfileRole;
import org.example.enums.ProfileStatus;
import org.example.exp.AppBadException;
import org.example.repository.EmailHistoryRepository;
import org.example.repository.ProfileRepository;
import org.example.service.history.EmailHistoryService;
import org.example.utils.JwtUtil;
import org.example.utils.MD5Util;
import org.example.utils.RandomUtil;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

@Slf4j
@Service
public class AuthorizationService {

    private final ProfileRepository profileRepository;
    private final MailSenderService mailSenderService;
    private final EmailHistoryService emailHistoryService;
    private final EmailHistoryRepository emailHistoryRepository;

    public AuthorizationService(ProfileRepository profileRepository, MailSenderService mailSenderService, EmailHistoryService emailHistoryService, EmailHistoryRepository emailHistoryRepository) {
        this.profileRepository = profileRepository;
        this.mailSenderService = mailSenderService;
        this.emailHistoryService = emailHistoryService;
        this.emailHistoryRepository = emailHistoryRepository;
    }
    public String registration(RegistrationDTO dto) {
        Optional<ProfileEntity> optional = profileRepository.findByEmailAndVisibleTrue(dto.getEmail());
        if (optional.isPresent()) {
            log.warn("Email already exists email => {}", dto.getEmail());
            throw new AppBadException("Email already exists");
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
        return "To complete your registration please verify your phone.";
    }

    public String authorizationVerification(Long userId) {
        Optional<ProfileEntity> optional = profileRepository.findById(userId);
        if (optional.isEmpty()) {
            throw new AppBadException("User not found");
        }

        ProfileEntity entity = optional.get();
        if (!entity.getVisible() || !entity.getStatus().equals(ProfileStatus.REGISTRATION)) {
            throw new AppBadException("Registration not completed");
        }

        profileRepository.updateStatus(userId, ProfileStatus.ACTIVE);
        return "Success";
    }

    //login email
    public AuthorizationResponseDTO login(LoginDTO dto) {
        Optional<ProfileEntity> optional = profileRepository.findByEmailAndVisibleTrue(
                dto.getEmail());
        if (optional.isEmpty()) {
            throw new AppBadException("profile not found");
        }

        ProfileEntity entity = optional.get();
        if (!entity.getPassword().equals(MD5Util.getMD5(dto.getPassword()))) {
            throw new AppBadException("password does not match");
        }

        if (!entity.getStatus().equals(ProfileStatus.ACTIVE)) {
            throw new AppBadException("Wrong status");
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
    public String registrationResendEmail(String email) {
        Optional<ProfileEntity> optional = profileRepository.findByEmailAndVisibleTrue(email);
        if (optional.isEmpty()) {
            throw new AppBadException("Email not exists");
        }
        ProfileEntity entity = optional.get();
        emailHistoryService.isNotExpiredEmail(entity.getEmail()); // check for expireation date
        if (!entity.getVisible() || !entity.getStatus().equals(ProfileStatus.REGISTRATION)) {
            throw new AppBadException("Registration not completed");
        }

        Optional<EmailHistoryEntity> byEmail = emailHistoryRepository.findByEmail(email);
        EmailHistoryEntity emailHistory=byEmail.get();
        if (!emailHistory.getStatus().equals(EmailHistoryStatus.SENT)){
            throw new AppBadException("Email already exists");
        }
        emailHistoryService.checkEmailLimit(email);
        sendRegistrationRandomCodeEmail(entity.getId(), email);
        return "To complete your registration please verify your email.";
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
