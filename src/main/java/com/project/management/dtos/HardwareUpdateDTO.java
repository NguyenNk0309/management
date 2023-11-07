package com.project.management.dtos;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class HardwareUpdateDTO {
    private Integer V1; // gasSensorValue

    private Integer V2; // flameSensorValue

    private Float V3; // pressureSensorValue

    private Float V4; // ampSensorValue

    private Float V5; // temperatureSensorValue

    private Float V6; // humiditySensorValue

    private Float V7; // secondAmpSensorValue

    private Float V8; // powerConsumption

    private Float V9; // waterConsumption

    private Boolean V10; // acSwitch

    private Boolean V11; // acPumpSwitch

    private Boolean V12; // reservedSwitch


}
