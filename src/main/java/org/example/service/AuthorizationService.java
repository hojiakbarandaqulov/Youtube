package org.example.service;

import org.example.dto.AuthorizationResponseDTO;
import org.example.dto.LoginDTO;
import org.example.dto.RegistrationDTO;
import org.example.entity.ProfileEntity;
import org.example.enums.ProfileRole;
import org.example.enums.ProfileStatus;
import org.example.exp.AppBadException;
import org.example.repository.ProfileRepository;
import org.example.service.history.EmailHistoryService;
import org.example.utils.JwtUtil;
import org.example.utils.MD5Util;
import org.example.utils.RandomUtil;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthorizationService {
    private final ProfileRepository profileRepository;
    private final MailSenderService mailSenderService;
    private final EmailHistoryService emailHistoryService;
    public AuthorizationService(ProfileRepository profileRepository, MailSenderService mailSenderService, EmailHistoryService emailHistoryService) {
        this.profileRepository = profileRepository;
        this.mailSenderService = mailSenderService;
        this.emailHistoryService = emailHistoryService;
    }

    public String registration(RegistrationDTO registrationDTO) {
        Optional<ProfileEntity> byEmail = profileRepository.findByEmailAndVisibleTrue(registrationDTO.getEmail());
        if (byEmail.isPresent()) {
            throw new AppBadException("Email already in use");
        }
        ProfileEntity profileEntity = new ProfileEntity();
        profileEntity.setName(registrationDTO.getName());
        profileEntity.setSurname(registrationDTO.getSurname());
        profileEntity.setEmail(registrationDTO.getEmail());
        profileEntity.setPassword(registrationDTO.getPassword());
        profileEntity.setRole(ProfileRole.ROLE_USER);
        profileEntity.setStatus(ProfileStatus.REGISTRATION);
        profileRepository.save(profileEntity);
        sendRegistrationEmail(profileEntity.getId(), profileEntity.getEmail());

        return "Thank you for registering your profile";
    }


    public void sendRegistrationEmail(Long profileId, String email) {
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

    // resend email method
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

        emailHistoryService.checkEmailLimit(email);
        sendRegistrationRandomCodeEmail(entity.getId(), email);
        return "To complete your registration please verify your email.";
    }

    public void sendRegistrationRandomCodeEmail(Long profileId, String email) {
        // send email
        String url = "http://localhost:8080/auth/verification/" + profileId;
        String text = String.format(RandomUtil.getRandomSmsCode(), url);
        mailSenderService.send(email, "Complete registration", text);
        emailHistoryService.crete(email, text); // create history
    }

    public AuthorizationResponseDTO login(LoginDTO loginDTO) {
        Optional<ProfileEntity> optional = profileRepository.findByEmailAndVisibleTrue(loginDTO.getEmail());
        if (optional.isEmpty()) {
            throw new AppBadException("Email not exists");
        }
        ProfileEntity entity = optional.get();
        if (!entity.getPassword().equals(MD5Util.getMD5(loginDTO.getPassword()))) {
            throw new AppBadException("Wrong password");
        }
        if (!entity.getStatus().equals(ProfileStatus.ACTIVE)) {
            throw new AppBadException("Wrong status");
        }
        AuthorizationResponseDTO responseDTO=new AuthorizationResponseDTO();
        responseDTO.setId(entity.getId());
        responseDTO.setName(entity.getName());
        responseDTO.setSurname(entity.getSurname());
        responseDTO.setRole(entity.getRole());
        responseDTO.setJwt(JwtUtil.encode(responseDTO.getId(), entity.getEmail(), responseDTO.getRole()));
        return responseDTO;
    }
}
