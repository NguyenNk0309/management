package com.project.management.configs;

import com.project.management.entities.Role;
import com.project.management.entities.User;
import com.project.management.enums.RoleEnum;
import com.project.management.repositories.HardwareRepository;
import com.project.management.repositories.RoleRepository;
import com.project.management.repositories.RoomRepository;
import com.project.management.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Optional;

@Component
public class PopulateData implements ApplicationRunner {
    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private HardwareRepository hardwareRepository;

    @Override
    public void run(ApplicationArguments args) throws Exception {
//        Role
        Optional<Role> adminRole = roleRepository.findByName(RoleEnum.ADMIN.desc);
        Optional<Role> userRole = roleRepository.findByName(RoleEnum.USER.desc);

        if (adminRole.isEmpty() && userRole.isEmpty()) {
            roleRepository.save(Role.builder().name(RoleEnum.ADMIN.desc).build());
            roleRepository.save(Role.builder().name(RoleEnum.USER.desc).build());
        }

//        User
        Optional<User> adminUser = userRepository.findByUsername("admin");

        if (adminUser.isEmpty()) {
            userRepository.save(User.builder()
                    .username("admin")
                    .password(passwordEncoder.encode("admin"))
                    .address("236 Ha Huy Giap, P.Thanh Loc, Q.12, TP.HCM")
                    .email("khoinguyen.030901@gmail.com")
                    .phoneNumber("0767503530")
                    .fullName("Nguyen Khoi Nguyen")
                    .roles(Collections.singletonList(adminRole.get()))
                    .build());
        }

    }

}
