package org.example.dto;

import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Data;
import org.example.enums.ProfileRole;
import org.example.enums.ProfileStatus;
import java.time.LocalDateTime;

@Data
public class ProfileDTO {
    private Long id;
    private String name;
    private String surname;
    private String email;
    private String password;
    private Integer photo;
    private ProfileRole role;
    private ProfileStatus status;
    private LocalDateTime createdDate=LocalDateTime.now();
    private Boolean visible=Boolean.TRUE;

}
