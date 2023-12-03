package com.project.management.entities;

import lombok.*;
import org.hibernate.envers.Audited;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Audited
public class Hardware extends BaseEntity {

    @OneToOne(mappedBy = "hardware", fetch = FetchType.LAZY)
    private Room room;

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
    private Float totalPowerConsumption;
    private Float totalWaterConsumption;


    @OneToMany(mappedBy = "hardware", fetch = FetchType.LAZY, cascade = CascadeType.PERSIST, orphanRemoval = true)
    private List<HardwareLimit> limitList = new ArrayList<>();

}
