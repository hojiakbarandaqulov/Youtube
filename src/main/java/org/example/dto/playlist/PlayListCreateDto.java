package org.example.dto.playlist;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.example.enums.PlayListStatus;

@Getter
@Setter
public class PlayListCreateDto {
    @Size(min = 3, message = "name must be at least 3 characters")
    @NotBlank(message = "name is required")
    private String name;

    @Size(min = 10, message = "description must be at least 10 characters")
    @NotBlank(message = "some description is required")
    private String description;

    @NotNull(message = "status reqiured")
    private PlayListStatus status;

    @NotNull(message = "order number reqiured")
    private int orderNumber;

    @NotNull(message = "chanel id reqiured")
    private String chanelId;
}
