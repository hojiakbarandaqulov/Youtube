package org.example.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.dto.profile.ProfileChangePasswordDTO;
import org.example.dto.profile.ProfileCreateDTO;
import org.example.dto.profile.ProfileDTO;
import org.example.dto.profile.ProfileUpdateDTO;
import org.example.service.ProfileService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/profile")
@RequiredArgsConstructor
public class ProfileController {

    private final ProfileService profileService;

    @PostMapping("/change/password")
    public ResponseEntity<String> changePassword(@RequestBody ProfileChangePasswordDTO dto) {
        String response = profileService.changePassword(dto);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/change/email")
    public ResponseEntity<String> changeEmail(@RequestParam String newEmail) {
        String response = profileService.changeEmail(newEmail);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/verification/{token}")
    public ResponseEntity<String> verifyEmail(@PathVariable String token) {
        String response = profileService.verifyEmail(token);
        return ResponseEntity.ok(response);
    }
    @PutMapping("/update/detail")
    public ResponseEntity<ProfileDTO> update(@RequestBody ProfileUpdateDTO dto) {
        ProfileDTO response = profileService.update(dto);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/getProfileDetail")
    public ResponseEntity<ProfileDTO> getProfileDetail() {
        return ResponseEntity.ok().body(profileService.getProfileDetail());
    }
    @Autowired
    private ProfileService profileService;
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(value = "/adm/create") //ADMIN
    public ResponseEntity<ProfileDTO> create(@Valid @RequestBody ProfileCreateDTO dto) {
        ProfileDTO response = profileService.create(dto);
        return ResponseEntity.ok(response);
    }


}
