package com.project.management.dtos;

import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.Date;

@Getter
@Setter
@SuperBuilder
public class UserInfoDTO extends UserGeneralInfoDTO {

    private Long pk;

    private Date createdOn;

    private Date updatedOn;
}
