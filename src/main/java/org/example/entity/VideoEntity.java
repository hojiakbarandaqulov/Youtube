package org.example.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.example.entity.category.CategoryEntity;
import org.example.enums.VideoStatus;
import org.example.enums.VideoType;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "video")
public class VideoEntity {
    @Id
    @GeneratedValue(generator = "uuid-hibernate-generator")
    @GenericGenerator(name = "uuid-hibernate-generator", strategy = "org.hibernate.id.UUIDGenerator")
    private String id;

    @Column(name = "preview_attach_id")
    private String previewAttachId;

    @Column(name = "title", columnDefinition = "text")
    private String title;

    @Column(name = "category_id")
    private Integer categoryId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id",insertable = false, updatable = false)
    private CategoryEntity category;

    @Column(name = "attach_id")
    private String attachId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "attach_id", insertable = false, updatable = false)
    private AttachEntity attach;

    @Column(name = "created_date")
    private LocalDateTime createdDate = LocalDateTime.now();

    @Column(name = "published_date")
    private LocalDateTime publishedDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private VideoStatus status;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private VideoType type;

    @Column(name = "view_count")
    private Integer viewCount;

    @Column(name = "shared_count")
    private Integer sharedCount;

    @Column(name = "description",columnDefinition = "text")
    private String description;

    @Column(name = "channel_id")
    private String channelId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "channel_id",insertable = false, updatable = false)
    private ChannelEntity channel;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tag_id", insertable = false, updatable = false)
    private TagEntity tags;

    @Column(name ="like_count")
    private Integer likeCount;

    @Column(name = "dislike_count")
    private  Integer dislikeCount;


}
