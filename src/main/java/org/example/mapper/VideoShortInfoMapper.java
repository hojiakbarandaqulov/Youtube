package org.example.mapper;

import lombok.Getter;
import org.example.enums.VideoStatus;
import org.example.enums.VideoType;

import java.time.LocalDate;

public interface VideoShortInfoMapper {
//    String getId();
    String getTitle();
    String getPreviewAttachId();
    LocalDate getPublishedDate();
    String getChannelId();
    String getChannelName();
    String getChannelPhotoId();
    Integer getViewCount();
    String getDescription();
    VideoStatus getStatus();
    VideoType getType();
    String getAttachId();
    Integer getCategoryId();
}
