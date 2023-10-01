package com.project.management.entities;

import lombok.*;

import javax.persistence.*;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Room extends BaseEntity {
    private String uuid;

    private String name;

    @OneToMany(mappedBy = "room", fetch = FetchType.LAZY)
    private List<Hardware> hardwares;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_fk")
    private User user;

    public Room(String name, User user) {
        this.name = name;
        this.user = user;
        this.uuid = UUID.randomUUID().toString();
    }
}
