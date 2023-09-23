package com.project.management.dtos;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ResponseDTO {
    private Integer status;
    private String message;
    private Object data;
}
