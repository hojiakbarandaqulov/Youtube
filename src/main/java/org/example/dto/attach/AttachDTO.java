package org.example.dto.attach;

import jakarta.persistence.Column;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class AttachDTO {
    private String id;
    private String originName;
    private Long size;
    private String type;
    private String path;
    private LocalDate duration;
    private LocalDateTime createdData;
    private String url;

}
