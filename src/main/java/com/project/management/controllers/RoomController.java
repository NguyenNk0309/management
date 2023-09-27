package com.project.management.controllers;

import com.project.management.dtos.ResponseDTO;
import com.project.management.services.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("room")
public class RoomController {
    @Autowired
    RoomService roomService;

    @PostMapping("create")
    @ResponseStatus(HttpStatus.OK)
    public ResponseDTO createNewRoom(
            @RequestParam(name = "room-name")String roomName,
            @RequestParam(name = "user-pk", required = false, defaultValue = "1")Long userPk) {
        return ResponseDTO.builder()
                .message("Success")
                .status(HttpStatus.OK.value())
                .data(roomService.createNewRoom(roomName, userPk))
                .build();
    }

}
