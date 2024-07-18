package org.example.mapper;

import java.time.LocalDateTime;

public interface VideoFullInfoMapper {
    String getId();
    String getTitle();
    String getDescription();
    String getPreviewAttachId();
    String getAttachId();
    Integer getCategoryId();
    Integer getTagList();
    LocalDateTime getPublishedDate();
    String getChannel();
    Integer getViewCount();
    Integer getSharedCount();
    String getLike();
    String getDuration();

}
