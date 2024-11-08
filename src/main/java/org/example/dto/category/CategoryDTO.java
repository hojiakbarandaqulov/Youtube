package org.example.dto.category;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CategoryDTO {
    @NotBlank(message = "category name is required")
    private String name ;

}
