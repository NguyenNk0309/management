package com.project.management.dtos;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@Builder
public class RoomInfoDTO {
    Long pk;
    String token;
    String name;
    Date createdOn;
    Date updatedOn;
    UserInfoDTO user;
    Boolean isUsed;
}
