package org.example.dto.video;

import jakarta.mail.search.SearchTerm;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.example.enums.VideoStatus;
import org.example.enums.VideoType;

import java.time.LocalDateTime;

@Data
public class VideoDTO {
    @NotBlank(message = "title required")
    private String title;
    @NotBlank(message = "previewAttachId required")
    private String previewAttachId;
    @NotBlank(message = "description required")
    private String description;
    @NotBlank(message = "channelId required")
    private String channelId;
    @NotBlank(message = "type required")
    private VideoType type;
    @NotBlank(message = "status required")
    private VideoStatus status;
    @NotBlank(message = "attachId required")
    private String attachId;
    @NotBlank(message = "categoryId required")
    private Integer categoryId;

}
