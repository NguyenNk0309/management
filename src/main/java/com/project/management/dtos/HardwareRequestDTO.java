package com.project.management.dtos;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class HardwareRequestDTO {
    private Boolean acSwitch;

    private Boolean acPumpSwitch;

    private Boolean reservedSwitch;

    private Boolean isShutdown;

    private Boolean isReboot;
}
