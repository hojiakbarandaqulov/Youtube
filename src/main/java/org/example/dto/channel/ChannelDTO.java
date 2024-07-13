package org.example.dto.channel;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import org.example.dto.AttachDTO;
import org.example.enums.ChannelStatus;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ChannelDTO {
    private String id;
    private String name;
    private AttachDTO photo;
    private String photoId;
    private String description;
    private ChannelStatus status;
    private String banner;
    private Integer profileId;
}
