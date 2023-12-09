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
    private Float voltageSensorValue;
    private Float ampereSensorValue;
    private Float temperatureSensorValue;
    private Float humiditySensorValue;
    private Float waterSensorValue;
    private Float fireSensor1Value;
    private Float fireSensor2Value;
    private Float fireSensor3Value;
    private Float fireSensor4Value;
    private Float fireSensor5Value;
    private Boolean acSwitch1;
    private Boolean acSwitch2;
    private Boolean userReq;
    private Boolean resetFactoryReq;
    private Boolean rebootReq;
    private Float totalPowerConsumption;
    private Float totalWaterConsumption;
    private Date updatedOn;
}
