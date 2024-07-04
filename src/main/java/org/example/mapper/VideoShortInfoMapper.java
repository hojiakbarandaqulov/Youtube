package org.example.mapper;

import lombok.Getter;

import java.time.LocalDate;

public interface VideoShortInfoMapper {
    String getId();
    String getTitle();
    String getPreviewAttachId();
    LocalDate getPublishedDate();
    String getChannelId();
    String getChannelName();
    String getChannelPhotoId();
    Integer getViewCount();
    LocalDate getDuration();
}
