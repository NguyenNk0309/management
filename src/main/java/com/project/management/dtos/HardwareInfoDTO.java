package com.project.management.dtos;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Builder
@Getter
@Setter
public class HardwareInfoDTO {
    private Float gasSensorValue;

    private Float flameSensorValue;

    private Float pressureSensorValue;

    private Float ampSensorValue;

    private Float temperatureSensorValue;

    private Float humiditySensorValue;

    private Float secondAmpSensorValue;

    private Boolean acSwitch;

    private Boolean acPumpSwitch;

    private Float powerConsumption;

    private Float waterConsumption;

    private Date updatedOn;
}
