package org.example.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "attach")
public class AttachEntity {

    @Id
    @Column(length = 255)
    private String id;

    @Column(name = "orginal_name")
    private String originalName;

    @Column(name = "size")
    private Long size;

    @Column(name = "extension")
    private String extension;

    @Column(name = "path")
    private String path;

    @Column(name = "created_date")
    private LocalDateTime createdDate = LocalDateTime.now();

}
