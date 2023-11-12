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

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_fk")
    private Room room;

    private Integer gasSensorValue;

    private Integer flameSensorValue;

    private Float pressureSensorValue;

    private Float ampSensorValue;

    private Float temperatureSensorValue;

    private Float humiditySensorValue;

    private Float secondAmpSensorValue;

    private Float powerConsumption;

    private Float waterConsumption;

    private Boolean acSwitch;

    private Boolean acPumpSwitch;

    private Boolean isShutdown;

    private Boolean isReboot;

    @OneToMany(mappedBy = "hardware", fetch = FetchType.LAZY)
    private List<HardwareLimit> limitList = new ArrayList<>();

}
