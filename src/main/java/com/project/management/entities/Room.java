package com.project.management.entities;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Room extends BaseEntity {
    private String uuid;

    private String name;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_fk")
    private User user;
}
