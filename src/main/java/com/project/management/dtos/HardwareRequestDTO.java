package com.project.management.dtos;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class HardwareRequestDTO {
    private Boolean acSwitch1;

    private Boolean acSwitch2;

    private Boolean userReq;

    private Boolean resetFactoryReq;

    private Boolean rebootReq;
}
