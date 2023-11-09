package com.project.management.dtos;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class HardwareUpdateDTO {
    private Integer V0; // gasSensorValue

    private Integer V1; // flameSensorValue

    private Float V2; // pressureSensorValue

    private Float V3; // ampSensorValue

    private Float V4; // temperatureSensorValue

    private Float V5; // humiditySensorValue

    private Float V6; // secondAmpSensorValue

    private Float V7; // powerConsumption

    private Float V8; // waterConsumption

    private Boolean V9; // acSwitch

    private Boolean V10; // acPumpSwitch


}
