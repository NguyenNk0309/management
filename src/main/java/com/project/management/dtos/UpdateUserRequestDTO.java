package com.project.management.dtos;

import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateUserRequestDTO extends UserGeneralInfoDTO {

    private String currentPassword;

    private String newPassword;

}
