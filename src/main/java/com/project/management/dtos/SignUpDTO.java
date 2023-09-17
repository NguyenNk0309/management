package com.project.management.dtos;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SignUpDTO {
    private String username;

    private String password;

    private String address;

    private String phoneNumber;

    private String email;

    private String fullName;
}
