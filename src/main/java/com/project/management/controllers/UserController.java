package com.project.management.controllers;

import com.project.management.dtos.ResponseDTO;
import com.project.management.dtos.UpdateUserRequestDTO;
import com.project.management.dtos.UserInfoDTO;
import com.project.management.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("user")
public class UserController {
    @Autowired
    UserService userService;

    @GetMapping("my-info")
    @ResponseStatus(HttpStatus.OK)
    public ResponseDTO getMyInfo() {
        UserInfoDTO user = userService.getMyInfo();
        return ResponseDTO.builder()
                        .status(HttpStatus.OK.value())
                        .message("Success")
                        .data(user)
                        .build();
    }

    @GetMapping("{pk}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseDTO getUserByPk(@PathVariable Long pk) {
        UserInfoDTO user = userService.getUserByPk(pk);
        return ResponseDTO.builder()
                .status(HttpStatus.OK.value())
                .message("Success")
                .data(user)
                .build();
    }

    @GetMapping("all")
    @ResponseStatus(HttpStatus.OK)
    public ResponseDTO getAllUsers() {
        List<UserInfoDTO> users = userService.getAllUsers();
        return ResponseDTO.builder()
                .status(HttpStatus.OK.value())
                .message("Success")
                .data(users)
                .build();
    }

    @PutMapping("update/{pk}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseDTO updateUserByPk(@PathVariable Long pk, @RequestBody UpdateUserRequestDTO requestDTO) {
        userService.updateUserByPk(pk, requestDTO);
        return ResponseDTO.builder()
                .status(HttpStatus.OK.value())
                .message("Success")
                .data(null)
                .build();
    }

    @DeleteMapping("delete/{pk}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseDTO updateUserByPk(@PathVariable Long pk) {
        userService.deleteUserByPk(pk);
        return ResponseDTO.builder()
                .status(HttpStatus.OK.value())
                .message("Success")
                .data(null)
                .build();
    }
}
