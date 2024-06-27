package org.example.dto;

import lombok.Getter;
import lombok.Setter;
import org.example.enums.ProfileRole;

@Getter
@Setter
public class JwtDTO {
    private Long id;
    private String username;
    private ProfileRole role;

    public JwtDTO(Long id, String userName, ProfileRole role) {
        this.id = id;
        this.username = userName;
        this.role = role;
    }

    public JwtDTO(Long id) {
        this.id = id;
    }
}
