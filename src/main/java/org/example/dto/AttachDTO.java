package org.example.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AttachDTO {
    private String id;
    private String originalName;
    private String path;
    private Long size;
    private String extension;
    private LocalDateTime createdData;
    private String url;
    public AttachDTO(String id, String s) {
    }


    public AttachDTO() {

    }
}
