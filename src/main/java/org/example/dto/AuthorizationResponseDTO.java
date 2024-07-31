package org.example.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.example.enums.ProfileRole;

@Data
public class AuthorizationResponseDTO {
    @NotNull
    private Long id;
    @NotBlank(message = "name required")
    private String name;
    @NotBlank(message = "surname required")
    private String surname;
    @NotBlank(message = "role required")
    private ProfileRole role;
    private String jwt;

}
