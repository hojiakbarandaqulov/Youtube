package org.example.dto.filter;

import org.example.entity.profile.ProfileEntity;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
@Getter
@Setter

public class CommentFilterDTO {
    private Integer id;
    private LocalDateTime created_date;
    private LocalDateTime update_date;
    private ProfileEntity profile_id;
    private String content;
    private String article_id;
    private String reply_id;
    private Boolean visible;
}
