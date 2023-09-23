package com.project.management.dtos;

import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
public class UpdateUserRequestDTO extends UserGeneralInfoDTO {

    private String password;

}
