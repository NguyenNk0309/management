package com.project.management.entities;

import lombok.*;
import org.hibernate.envers.Audited;

import javax.persistence.*;
import java.util.List;
import java.util.UUID;
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

    @OneToOne(mappedBy = "room", fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    private Hardware hardware;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_fk")
    private User user;

    public Room(String name, User user) {
        this.name = name;
        this.user = user;
        this.registerToken = UUID.randomUUID().toString();
        this.apiToken = generateRandomToken(5);
        this.isUsed = false;
    }
}
