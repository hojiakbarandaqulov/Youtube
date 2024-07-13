package org.example.dto.playlist;

import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
@Setter
@Getter
public class PlayListUpdateDto {
    @Size(min = 3, message = "name must be at least 3 characters")
    private String name;

    @Size(min = 10, message = "description must be at least 10 characters")
    private String description;

    private Integer orderNumber;

}
