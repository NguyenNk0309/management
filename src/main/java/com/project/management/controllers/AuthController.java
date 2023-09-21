package com.project.management.controllers;

import com.project.management.dtos.ResponseDTO;
import com.project.management.dtos.SignInDTO;
import com.project.management.dtos.SignUpDTO;
import com.project.management.services.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;

@RestController
@RequestMapping("auth")
public class AuthController {
    @Autowired
    private AuthService authService;

    @PostMapping("sign-up")
    @ResponseStatus(HttpStatus.OK)
    public ResponseDTO signUp(@RequestBody SignUpDTO signUpDTO) {
        authService.signUp(signUpDTO);
        return ResponseDTO.builder()
                .status(HttpStatus.OK.value())
                .message("Success")
                .data(null)
                .build();

    }

    @PostMapping("sign-in")
    @ResponseStatus(HttpStatus.OK)
    public ResponseDTO login(@RequestBody SignInDTO signInDTO){
        String token = authService.signIn(signInDTO);
        return ResponseDTO.builder()
                .status(HttpStatus.OK.value())
                .message("Success")
                .data(token)
                .build();
    }
}
