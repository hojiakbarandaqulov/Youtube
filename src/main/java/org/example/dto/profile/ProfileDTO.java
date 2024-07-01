package org.example.dto.profile;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import org.example.enums.ProfileRole;
import org.example.enums.ProfileStatus;

import java.time.LocalDateTime;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProfileDTO {
    private Long id;
    private String name;
    private String surname;
    private String email;
    private String password;
    private Integer photo;
    private LocalDateTime createdDate=LocalDateTime.now();
    private ProfileRole role;
    private String jwt;
    private ProfileStatus status;
    private Boolean visible=Boolean.TRUE;
}
