package com.project.management.entities;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Role extends BaseEntity {
    private String name;

    @ManyToMany(mappedBy = "roles", fetch = FetchType.EAGER)
    private List<User> users = new ArrayList<>();
}
