package com.project.management.controllers;

import com.project.management.dtos.ResponseDTO;
import com.project.management.dtos.UserInfoDTO;
import com.project.management.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("user")
public class UserController {
    @Autowired
    UserService userService;

    @GetMapping("me")
    @ResponseStatus(HttpStatus.OK)
    public ResponseDTO getMyInfo() {
        UserInfoDTO user = userService.getMyInfo();
        return ResponseDTO.builder()
                        .status(HttpStatus.OK.value())
                        .message("Success")
                        .data(user)
                        .build();
    }
}
