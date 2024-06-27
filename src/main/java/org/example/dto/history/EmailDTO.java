package org.example.dto.history;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EmailDTO {
    @NotNull
    private Long id;

    @NotBlank(message = "email required")
    private String email;

    @NotBlank(message = "title required")
    private String title;

    @NotBlank(message = "message required")
    private String message;

    @NotBlank(message = "CreatedDate required")
    private LocalDateTime createdDate;

}
