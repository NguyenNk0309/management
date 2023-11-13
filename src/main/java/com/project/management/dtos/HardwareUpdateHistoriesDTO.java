package com.project.management.dtos;

import java.util.Date;

public interface HardwareUpdateHistoriesDTO {
    Float getGasSensorValue();

    Float getFlameSensorValue();

    Float getPressureSensorValue();

    Float getAmpSensorValue();

    Float getTemperatureSensorValue();

    Float getHumiditySensorValue();

    Float getSecondAmpSensorValue();

    Long getDateOfWeek();

}
