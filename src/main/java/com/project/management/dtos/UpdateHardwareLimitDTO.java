package com.project.management.dtos;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class UpdateHardwareLimitDTO {
    private String hardwareId;

    private Float upperLimit;

    private Float lowerLimit;
}
