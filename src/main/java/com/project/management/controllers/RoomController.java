package com.project.management.controllers;

import com.project.management.dtos.HardwareRequestDTO;
import com.project.management.dtos.ResponseDTO;
import com.project.management.dtos.HardwareLimitDTO;
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

    @GetMapping("all")
    @ResponseStatus(HttpStatus.OK)
    public ResponseDTO getAllRooms() {
        return ResponseDTO.builder()
                .message("Success")
                .status(HttpStatus.OK.value())
                .data(roomService.getAllRooms())
                .build();
    }

    @GetMapping("of-user/{pk}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseDTO getRoomsByUserPk(@PathVariable Long pk) {
        return ResponseDTO.builder()
                .message("Success")
                .status(HttpStatus.OK.value())
                .data(roomService.getRoomsByUserPk(pk))
                .build();
    }

    @PutMapping("updateHardware/{pk}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseDTO updateHardware(@PathVariable Long pk, @RequestBody HardwareRequestDTO requestDTO) {
        roomService.updateHardware(pk, requestDTO);
        return ResponseDTO.builder()
                .message("Success")
                .status(HttpStatus.OK.value())
                .data(null)
                .build();
    }

    @GetMapping("{pk}/hardware-histories-of-week")
    @ResponseStatus(HttpStatus.OK)
    public ResponseDTO getHardwareHistoriesByRoomPk(@PathVariable Long pk,
                                                    @RequestParam(name = "week")Long week) {
        return ResponseDTO.builder()
                .message("Success")
                .status(HttpStatus.OK.value())
                .data(roomService.getHardwareHistoriesByRoomPk(pk, week))
                .build();
    }

    @GetMapping("{pk}/power-water-consumption-histories")
    @ResponseStatus(HttpStatus.OK)
    public ResponseDTO getPowerAndWaterConsumptionHistoriesByRoomPk(
            @PathVariable Long pk,
            @RequestParam(name = "timeType", required = true)String timeType,
            @RequestParam(name = "timeFilter", required = true)String timeFilter
    ) {
        return ResponseDTO.builder()
                .message("Success")
                .status(HttpStatus.OK.value())
                .data(roomService.getPowerAndWaterConsumptionHistoriesByRoomPk(pk, timeType, timeFilter))
                .build();
    }

    @DeleteMapping("delete/{pk}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseDTO deleteRoomByPk(@PathVariable Long pk) {
        roomService.deleteRoomByPk(pk);
        return ResponseDTO.builder()
                .message("Success")
                .status(HttpStatus.OK.value())
                .data(null)
                .build();
    }

    @PutMapping("update/{pk}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseDTO updateRoomByPk(@PathVariable Long pk, @RequestParam(name = "name") String name) {
        roomService.updateRoomByPk(pk, name);
        return ResponseDTO.builder()
                .message("Success")
                .status(HttpStatus.OK.value())
                .data(null)
                .build();
    }

    @PutMapping("update/hardware-limit/{pk}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseDTO updateHardwareLimit(@PathVariable Long pk, @RequestBody HardwareLimitDTO requestDTO) {
        roomService.updateHardwareLimit(pk, requestDTO);
        return ResponseDTO.builder()
                .message("Success")
                .status(HttpStatus.OK.value())
                .data(null)
                .build();
    }

    @DeleteMapping("delete/hardware-limit/{pk}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseDTO deleteHardwareLimit(@PathVariable Long pk, @RequestParam(name = "hardwareId") String hardwareId) {
        roomService.deleteHardwareLimit(pk, hardwareId);
        return ResponseDTO.builder()
                .message("Success")
                .status(HttpStatus.OK.value())
                .data(null)
                .build();
    }

    @GetMapping("get/hardware-limit/{pk}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseDTO getHardwareLimit(@PathVariable Long pk) {
        return ResponseDTO.builder()
                .message("Success")
                .status(HttpStatus.OK.value())
                .data(roomService.getHardwareLimit(pk))
                .build();
    }

}
