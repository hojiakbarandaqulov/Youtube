package org.example.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.example.dto.auth.AuthorizationResponseDTO;
import org.example.dto.auth.LoginDTO;
import org.example.dto.auth.RegistrationDTO;
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
    public ResponseEntity<String> registration(@Valid @RequestBody RegistrationDTO registrationDTO){
        String registration = authorizationService.registration(registrationDTO);
        log.info("Registration name = {} email = {}",registrationDTO.getName(), registrationDTO.getEmail());
        return ResponseEntity.ok().body(registration);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthorizationResponseDTO> login(@Valid @RequestBody LoginDTO loginDTO){
        AuthorizationResponseDTO login = authorizationService.login(loginDTO);
        return ResponseEntity.ok().body(login);
    }

    @GetMapping("/verification/{userId}")
    public ResponseEntity<String> verification(@PathVariable("userId") Long userId) {
        String body = authorizationService.authorizationVerification(userId);
        return ResponseEntity.ok().body(body);
    }

    @GetMapping("/registration/resend/{email}")
    public ResponseEntity<String> registrationResend(@PathVariable("email") String email) {
        String body = authorizationService.registrationResendEmail(email);
        return ResponseEntity.ok().body(body);
    }

}
