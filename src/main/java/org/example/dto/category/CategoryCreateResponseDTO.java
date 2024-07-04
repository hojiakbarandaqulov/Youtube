package org.example.dto.category;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CategoryCreateResponseDTO {
    private Integer id;
    private String name;
    private LocalDateTime createdDate;
}
