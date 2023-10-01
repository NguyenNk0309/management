package com.project.management.entities;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Hardware extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_fk")
    private Room room;

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
    
}
