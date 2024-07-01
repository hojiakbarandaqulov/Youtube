package org.example.dto.auth;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginDTO {
    @NotBlank(message = "email required")
    private String email;
    @NotBlank(message = "password required")
    private String password;
}
