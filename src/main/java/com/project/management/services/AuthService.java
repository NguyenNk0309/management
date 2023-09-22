package com.project.management.services;

import com.project.management.configs.JwtGenerator;
import com.project.management.dtos.SignInDTO;
import com.project.management.dtos.SignUpDTO;
import com.project.management.entities.Role;
import com.project.management.entities.User;
import com.project.management.enums.RoleEnum;
import com.project.management.exception.MyException;
import com.project.management.repositories.RoleRepository;
import com.project.management.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class AuthService {
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtGenerator jwtGenerator;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    public void signUp(SignUpDTO signUpDTO) {
        if (userRepository.existsByUsername(signUpDTO.getUsername())) {
            throw new MyException(HttpStatus.NOT_FOUND, "Username Already Exist");
        }

        User user = User.builder()
                .username(signUpDTO.getUsername())
                .password(passwordEncoder.encode(signUpDTO.getPassword()))
                .phoneNumber(signUpDTO.getPhoneNumber())
                .fullName(signUpDTO.getFullName())
                .email(signUpDTO.getEmail())
                .address(signUpDTO.getAddress())
                .build();

        Role roles = roleRepository.findByName(RoleEnum.USER.desc)
                .orElseThrow(() -> new MyException(HttpStatus.NOT_FOUND, "Role not found"));
        user.setRoles(Collections.singletonList(roles));

        userRepository.save(user);
    }

    public String signIn(SignInDTO signInDTO) {
        Authentication authentication;
        try {
             authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            signInDTO.getUsername(),
                            signInDTO.getPassword()));

        } catch (Exception e) {
            throw new MyException(HttpStatus.UNAUTHORIZED, "Username Or Password Is Incorrect");
        }
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return jwtGenerator.generateToken(authentication);
    }
}
