package org.example.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.example.entity.profile.ProfileEntity;
import org.example.enums.ChannelStatus;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "channel")
public class ChannelEntity {
    @Id
    @GeneratedValue(generator = "uuid-hibernate-generator")
    @GenericGenerator(name = "uuid-hibernate-generator", strategy = "org.hibernate.id.UUIDGenerator")
    private String id;

    @Column(name = "name")
    private String name;

    @Column(name = "photo_id")
    private String photoId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "photo_id",insertable=false,updatable=false)
    private AttachEntity photo;

    @Column(name = "description")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private ChannelStatus status;

    @Column(name = "banner_id")
    private String bannerId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "banner_id",insertable=false,updatable=false)
    private AttachEntity banner;

    @Column(name = "profile_id")
    private Integer profileId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "profile_id", insertable=false, updatable=false)
    private ProfileEntity profile;

    @Column(name = "created_date")
    private LocalDateTime createdDate=LocalDateTime.now();

}
