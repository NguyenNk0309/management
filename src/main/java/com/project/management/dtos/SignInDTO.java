package com.project.management.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class SignInDTO {
    @Schema(type = "string", example = "admin")
    private String username;

    @Schema(type = "string", example = "admin")
    private String password;
}
