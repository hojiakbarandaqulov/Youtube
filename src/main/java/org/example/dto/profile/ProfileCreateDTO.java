package org.example.dto.profile;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import org.example.enums.ProfileRole;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProfileCreateDTO {
    //id,name,surname,email,Role
    private Integer id;
    private String name;
    private String surname;
    private String email;
    private ProfileRole role;
}
