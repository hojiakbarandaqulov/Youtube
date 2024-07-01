package org.example.dto.profile;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProfileChangePasswordDTO {
    @Size(min = 4, message = "password uzunligi 4 dan kichik ")
    private String oldPassword;
    @Size(min = 4, message = "password uzunligi 4 dan kichik ")
    private String newPassword;
}
