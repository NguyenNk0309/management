package com.project.management.entities;

import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.*;
import java.util.stream.Collectors;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User extends BaseEntity implements UserDetails {
    private String username;

    private String password;

    private String address;

    private String phoneNumber;

    private String email;

    private String fullName;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_role",
            joinColumns = @JoinColumn(name = "user_fk", referencedColumnName = "pk"),
            inverseJoinColumns = @JoinColumn(name = "role_fk", referencedColumnName = "pk")
    )
    private List<Role> roles;

    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER)
    private Set<Room>  rooms;

//  -------------------------------------------------

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles.stream().map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList());
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
