package com.project.management.controllers;

import com.project.management.dtos.HardwareUpdateDTO;
import com.project.management.dtos.ResponseDTO;
import com.project.management.services.ExternalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("external/api")
public class ExternalController {
    @Autowired
    ExternalService externalService;

    @PostMapping("connect")
    @ResponseStatus(HttpStatus.OK)
    public ResponseDTO connectHardware(@RequestParam(name = "token") String token) {

        return ResponseDTO
                .builder()
                .message("Success")
                .status(HttpStatus.OK.value())
                .data(externalService.connectHardware(token))
                .build();
    }

    @GetMapping("master-request")
    @ResponseStatus(HttpStatus.OK)
    public ResponseDTO getRequest(@RequestParam(name = "token") String token) {

        return ResponseDTO
                .builder()
                .message("Success")
                .status(HttpStatus.OK.value())
                .data(externalService.getRequest(token))
                .build();
    }

    @PostMapping("update")
    @ResponseStatus(HttpStatus.OK)
    public ResponseDTO updateHardwareValue(
            @RequestParam(name = "token") String token,
            @RequestBody HardwareUpdateDTO hardwareInfoDTO) {
        externalService.updateHardwareValue(
                token,
                hardwareInfoDTO.getV0(),
                hardwareInfoDTO.getV1(),
                hardwareInfoDTO.getV2(),
                hardwareInfoDTO.getV3(),
                hardwareInfoDTO.getV4(),
                hardwareInfoDTO.getV5(),
                hardwareInfoDTO.getV6(),
                hardwareInfoDTO.getV7(),
                hardwareInfoDTO.getV8(),
                hardwareInfoDTO.getV9(),
                hardwareInfoDTO.getV10(),
                hardwareInfoDTO.getV11());
        
        return ResponseDTO
                .builder()
                .message("Success")
                .status(HttpStatus.OK.value())
                .data(null)
                .build();
    }
}
