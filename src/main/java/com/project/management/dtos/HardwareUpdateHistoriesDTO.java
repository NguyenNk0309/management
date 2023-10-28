package com.project.management.dtos;

import java.util.Date;

public interface HardwareUpdateHistoriesDTO {
    Integer getGasSensorValue();

    Integer getFlameSensorValue();

    Float getPressureSensorValue();

    Float getMotionSensorValue();

    Float getTemperatureSensorValue();

    Float getHumiditySensorValue();

    Float getSecondMotionSensorValue();
    Long getDateOfWeek();

}
