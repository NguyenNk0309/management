package com.project.management.dtos;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class UserInfoDTO {
    private String username;

    private String address;

    private String phoneNumber;

    private String email;

    private String fullName;
}
