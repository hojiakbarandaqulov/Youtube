package org.example.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.example.dto.AuthorizationResponseDTO;
import org.example.dto.LoginDTO;
import org.example.dto.RegistrationDTO;
import org.example.enums.LanguageEnum;
import org.example.service.AuthorizationService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@EnableMethodSecurity(prePostEnabled = true)
@RestController
@RequestMapping("/auth")
@Tag(name = "Auth Controller", description = "Api list for authorization, registration and other ....")
public class AuthorizationController {
    private final AuthorizationService authorizationService;

    public AuthorizationController(AuthorizationService authorizationService) {
        this.authorizationService = authorizationService;
    }

    @PostMapping("/registration")
    @Operation(summary = "Registration", description = "Api for profile registration")
    public ResponseEntity<String> registration(@Valid @RequestBody RegistrationDTO registrationDTO,
                                               @RequestHeader(value = "Accept-Language", defaultValue = "UZ") LanguageEnum language){
        log.info("Registration name = {} email = {}",registrationDTO.getName(), registrationDTO.getEmail());
        String registration = authorizationService.registration(registrationDTO, language);
        return ResponseEntity.ok().body(registration);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthorizationResponseDTO> login(@Valid @RequestBody LoginDTO loginDTO,
                                                          @RequestHeader(value = "Accept-Language", defaultValue = "UZ") LanguageEnum language){
        AuthorizationResponseDTO login = authorizationService.login(loginDTO,language);
        log .info("Login email = {} password = {}",loginDTO.getEmail(), loginDTO.getPassword());
        return ResponseEntity.ok().body(login);
    }

    @GetMapping("/verification/{userId}")
    public ResponseEntity<String> verification(@PathVariable("userId") Integer userId,
                                               @RequestHeader(value = "Accept-Language",defaultValue = "UZ")LanguageEnum language) {
        String body = authorizationService.authorizationVerification(userId, language);
        log.info("verification userId = {} email = {}",userId,body);
        return ResponseEntity.ok().body(body);
    }

    @GetMapping("/registration/resend/{email}")
    public ResponseEntity<String> registrationResend(@PathVariable("email") String email,
                                                     @RequestHeader(value = "Accept-Language", defaultValue = "UZ")LanguageEnum languageEnum) {
        String body = authorizationService.registrationResendEmail(email,languageEnum);
        log.info("registration userId = {} email = {}",email,body);
        return ResponseEntity.ok().body(body);
    }
}
