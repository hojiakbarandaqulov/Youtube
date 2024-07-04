package org.example.dto.video;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.example.enums.VideoStatus;
import org.example.enums.VideoType;

@Data
public class VideoUpdateDTO {
    @NotBlank(message = "title required")
    private String title;
    @NotBlank(message = "description required")
    private String description;
    private VideoType type;
    @NotBlank(message = "status required")
    private VideoStatus status;
    @NotBlank(message = "attachId required")
    private String attachId;
    @NotBlank(message = "categoryId required")
    private Integer categoryId;
}
