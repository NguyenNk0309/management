package com.project.management.entities;

import lombok.*;
import org.hibernate.envers.Audited;

import javax.persistence.*;

import static utils.ManagerUtil.generateRandomToken;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Audited
public class Room extends BaseEntity {
    private String registerToken;

    private String apiToken;

    private String name;

    private Boolean isUsed;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "hardware_fk")
    private Hardware hardware;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_fk")
    private User user;

    public Room(String name, User user) {
        this.name = name;
        this.user = user;
        this.registerToken = generateRandomToken(8);
        this.apiToken = generateRandomToken(5);
        this.isUsed = false;
    }
}
