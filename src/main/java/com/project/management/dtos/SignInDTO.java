package com.project.management.dtos;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SignInDTO {
    private String username;

    private String password;
}
