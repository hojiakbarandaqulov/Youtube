package org.example.dto.history;

import lombok.Data;

import java.time.LocalDateTime;
@Data
public class EmailFilterDTO {
    private String email;
    private LocalDateTime createdDate;
}
