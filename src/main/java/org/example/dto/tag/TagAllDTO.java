package org.example.dto.tag;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TagAllDTO {
    private Integer id;
    private String name;
    private LocalDateTime CreatedDate;
}
