package org.example.dto.category;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CategoryDTO {
    private Integer id ;

    @NotBlank(message = "category name is required")
    private String name ;

    private LocalDateTime createdDate;

}
