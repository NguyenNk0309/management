package com.project.management.dtos;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Builder
@Getter
@Setter
public class HardwareInfoDTO {
    private Integer gasSensorValue;

    private Integer flameSensorValue;

    private Float pressureSensorValue;

    private Float motionSensorValue;

    private Float temperatureSensorValue;

    private Float humiditySensorValue;

    private Float secondMotionSensorValue;

    private Boolean acSwitch;

    private Boolean acPumpSwitch;

    private Boolean reservedSwitch;

    private Date updatedOn;
}
