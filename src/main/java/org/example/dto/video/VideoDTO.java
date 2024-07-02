package org.example.dto.video;

import jakarta.persistence.*;
import lombok.Data;
import org.example.entity.AttachEntity;
import org.example.entity.category.CategoryEntity;
import org.example.enums.VideoStatus;
import org.example.enums.VideoType;

import java.time.LocalDateTime;

@Data
public class VideoDTO {
    private String previewAttachId;
    private String title;
    private Integer categoryId;
    private String attachId;
    private LocalDateTime createdDate;
    private LocalDateTime publishedDate;
    private VideoStatus status;
    private VideoType type;
    private String description;
    private String channelId;

}
