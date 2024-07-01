package org.example.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.example.entity.profile.ProfileEntity;
import org.example.enums.ChannelStatus;
import org.hibernate.annotations.GenericGenerator;
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "photo_id")
    private AttachEntity photo;

    @Column(name = "description")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private ChannelStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "banner_id")
    private AttachEntity banner;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "profile_id")
    private ProfileEntity profile_id;

}
