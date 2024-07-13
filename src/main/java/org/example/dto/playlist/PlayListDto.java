package org.example.dto.playlist;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import org.example.dto.channel.ChannelDTO;
import org.example.dto.profile.ProfileDTO;
import org.example.enums.PlayListStatus;

import java.time.LocalDateTime;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PlayListDto {
    private Long id;
    private String name;
    private String description;
    private PlayListStatus status;
    private String chanelId;
    private ChannelDTO chanel;
    private Long profileId;
    private ProfileDTO profile;
    private int orderNumber;
    private LocalDateTime createdDate;
    private Integer videoCount;
}
