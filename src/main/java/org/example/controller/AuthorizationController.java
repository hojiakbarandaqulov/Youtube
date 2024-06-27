package org.example.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.parser.Authorization;
import org.example.dto.AuthorizationResponseDTO;
import org.example.dto.LoginDTO;
import org.example.dto.RegistrationDTO;
import org.example.service.AuthorizationService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
    public ResponseEntity<String> registration(@RequestBody RegistrationDTO registrationDTO){
        String registration = authorizationService.registration(registrationDTO);
        return ResponseEntity.ok().body(registration);
    }
    @PreAuthorize("permitAll()")
    @PostMapping("/login")
    public ResponseEntity<AuthorizationResponseDTO> login( @RequestBody LoginDTO loginDTO){
        AuthorizationResponseDTO login = authorizationService.login(loginDTO);
        return ResponseEntity.ok().body(login);
    }

    @PostMapping("/registration")
    @Operation( summary = "Registration", description = "Api for profile registration")
    public ResponseEntity<String> registrationEmail(@Valid @RequestBody RegistrationDTO dto) {
        String body = authorizationService.registration(dto);
        log .info("Registration name = {} email = {}",dto.getName(), dto.getEmail());
        return ResponseEntity.ok().body(body);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthorizationResponseDTO> loginEmail(@Valid @RequestBody LoginDTO dto) {
        AuthorizationResponseDTO body = authorizationService.login(dto);
        return ResponseEntity.ok().body(body);
    }

//    @GetMapping("/verification/{userId}")
//    public ResponseEntity<String> verification(@PathVariable("userId") Integer userId) {
//        String body = authorizationService.authorizationVerification(userId);
//        return ResponseEntity.ok().body(body);
//    }

    @GetMapping("/registration/resend/{email}")
    public ResponseEntity<String> registrationResend(@PathVariable("email") String email) {
        String body = authorizationService.registrationResendEmail(email);
        return ResponseEntity.ok().body(body);
    }

}
