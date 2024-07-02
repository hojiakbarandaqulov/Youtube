package org.example.dto.video;

import lombok.Data;

@Data
public class VideoDTO {
    private String title;
    private String description;
    private int duration;
    private String url;
}
