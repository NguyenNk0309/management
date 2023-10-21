package com.project.management.configs;

import com.project.management.entities.Hardware;
import com.project.management.entities.Role;
import com.project.management.entities.Room;
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
import java.util.List;
import java.util.UUID;

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
        roleRepository.save(Role.builder().name(RoleEnum.ADMIN.desc).build());
        roleRepository.save(Role.builder().name(RoleEnum.USER.desc).build());

//        User
        Role adminRole = roleRepository.findByName(RoleEnum.ADMIN.desc).get();
        Role userRole = roleRepository.findByName(RoleEnum.USER.desc).get();
        userRepository.save(User.builder()
                .username("admin")
                .password(passwordEncoder.encode("admin"))
                .address("236 Ha Huy Giap, P.Thanh Loc, Q.12, TP.HCM")
                .email("khoinguyen.030901@gmail.com")
                .phoneNumber("0767503530")
                .fullName("Nguyen Khoi Nguyen")
                .roles(Collections.singletonList(adminRole))
                .build());

//        Room
        User user = userRepository.findById(1L).get();
        Room room1 = new Room("Room 1", user);
        room1.setIsUsed(true);
        roomRepository.save(room1);
        Room room2 = new Room("Room 2", user);
        room2.setIsUsed(true);
        roomRepository.save(room2);

//        Hardware
        Hardware hardware1 = Hardware.builder().room(room1).build();
        Hardware hardware2 = Hardware.builder().room(room2).build();
        hardwareRepository.saveAll(List.of(hardware1, hardware2));
    }

}
