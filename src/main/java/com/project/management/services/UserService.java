package com.project.management.services;

import com.project.management.dtos.UpdateUserRequestDTO;
import com.project.management.dtos.UserInfoDTO;
import com.project.management.entities.Role;
import com.project.management.entities.User;
import com.project.management.exception.MyException;
import com.project.management.repositories.RoleRepository;
import com.project.management.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public User loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Username Not Found"));
    }

    public UserInfoDTO getMyInfo() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new MyException(HttpStatus.NOT_FOUND, "Username Not Found"));
        return UserInfoDTO
                .builder()
                .pk(user.getPk())
                .createdOn(user.getCreateOn())
                .updatedOn(user.getUpdatedOn())
                .username(user.getUsername())
                .phoneNumber(user.getPhoneNumber())
                .fullName(user.getFullName())
                .address(user.getAddress())
                .email(user.getEmail())
                .role(user.getRoles().get(0).getName())
                .build();

    }

    public UserInfoDTO getUserByPk(Long pk) {
        User user = userRepository.findById(pk)
                .orElseThrow(() -> new MyException(HttpStatus.NOT_FOUND, String.format("User With Pk = %d Not Found", pk)));
        return UserInfoDTO
                .builder()
                .pk(user.getPk())
                .createdOn(user.getCreateOn())
                .updatedOn(user.getUpdatedOn())
                .username(user.getUsername())
                .phoneNumber(user.getPhoneNumber())
                .fullName(user.getFullName())
                .address(user.getAddress())
                .email(user.getEmail())
                .role(user.getRoles().get(0).getName())
                .build();
    }

    public void deleteUserByPk(Long pk) {
        userRepository.deleteById(pk);
    }

    public List<UserInfoDTO> getAllUsers() {
        List<User> users = userRepository.findAll();

        return users.stream()
                .map(user ->
                        UserInfoDTO.builder()
                                .pk(user.getPk())
                                .createdOn(user.getCreateOn())
                                .updatedOn(user.getUpdatedOn())
                                .username(user.getUsername())
                                .phoneNumber(user.getPhoneNumber())
                                .fullName(user.getFullName())
                                .address(user.getAddress())
                                .email(user.getEmail())
                                .role(user.getRoles().get(0).getName())
                                .build())
                .collect(Collectors.toList());
    }

    public void updateUserByPk(Long pk, UpdateUserRequestDTO requestDTO) {
        User user = userRepository.findById(pk)
                .orElseThrow(() -> new MyException(HttpStatus.NOT_FOUND, String.format("User With Pk = %d Not Found", pk)));

        user.setEmail(Objects.nonNull(requestDTO.getEmail()) ? requestDTO.getEmail() : user.getEmail());
        user.setAddress(Objects.nonNull(requestDTO.getAddress()) ? requestDTO.getAddress() : user.getAddress());
        user.setUsername(Objects.nonNull(requestDTO.getUsername()) ? requestDTO.getUsername() : user.getUsername());
        user.setFullName(Objects.nonNull(requestDTO.getFullName()) ? requestDTO.getFullName() : user.getFullName());
        user.setPhoneNumber(Objects.nonNull(requestDTO.getPhoneNumber()) ? requestDTO.getPhoneNumber() : user.getPhoneNumber());

        if (StringUtils.hasText(requestDTO.getRole())) {
            Role roles = roleRepository.findByName(requestDTO.getRole())
                    .orElseThrow(() -> new MyException(HttpStatus.NOT_FOUND, "Role Not Found"));
            user.getRoles().clear();
            user.getRoles().add(roles);
        }

        if (StringUtils.hasText(requestDTO.getCurrentPassword())) {
            if (!StringUtils.hasText(requestDTO.getNewPassword())) {
                throw new MyException(HttpStatus.BAD_REQUEST, "New Password Mustn't Be Empty");
            }

            if (passwordEncoder.matches(requestDTO.getCurrentPassword(), user.getPassword())) {
                user.setPassword(passwordEncoder.encode(requestDTO.getNewPassword()));
            } else {
                throw new MyException(HttpStatus.BAD_REQUEST, "Password Is Incorrect");
            }
        }

        userRepository.save(user);
    }
}
