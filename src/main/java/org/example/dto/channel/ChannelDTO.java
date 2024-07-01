package org.example.dto.channel;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import jakarta.persistence.*;
import lombok.Data;
import org.example.entity.AttachEntity;
import org.example.entity.profile.ProfileEntity;
import org.example.enums.ChannelStatus;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ChannelDTO {
    private String name;
    private String photo;
    private String description;
    private ChannelStatus status;
    private String banner;
    private Integer profileId;
}
