package com.project.management.services;

import com.project.management.dtos.UserInfoDTO;
import com.project.management.entities.User;
import com.project.management.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public User loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("Username not found"));
    }

    public UserInfoDTO getMyInfo() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("Username not found"));
        return UserInfoDTO
                .builder()
                .username(user.getUsername())
                .phoneNumber(user.getPhoneNumber())
                .fullName(user.getFullName())
                .address(user.getAddress())
                .email(user.getEmail())
                .build();

    }
}
