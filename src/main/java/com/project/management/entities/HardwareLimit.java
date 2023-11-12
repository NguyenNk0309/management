package com.project.management.entities;

import lombok.*;
import org.hibernate.envers.Audited;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Audited
public class HardwareLimit extends BaseEntity {
    private String hardwareId;

    private Number upperLimit;

    private Number lowerLimit;

    @ManyToOne
    @JoinColumn(name = "hardware_fk")
    private Hardware hardware;

}
