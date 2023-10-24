package com.project.management.dtos;

import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class UserGeneralInfoDTO {

    private String username;

    private String address;

    private String phoneNumber;

    private String email;

    private String fullName;

    private String role;
}
