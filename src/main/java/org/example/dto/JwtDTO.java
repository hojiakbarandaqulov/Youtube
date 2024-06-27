package org.example.dto;

import lombok.Getter;
import lombok.Setter;
import org.example.enums.ProfileRole;

@Getter
@Setter
public class JwtDTO {
    private Integer id;
    private String username;
    private ProfileRole role;

    public JwtDTO(Integer id, String userName, ProfileRole role) {
        this.id = id;
        this.username = userName;
        this.role = role;
    }

    public JwtDTO(Integer id) {
        this.id = id;
    }
}
