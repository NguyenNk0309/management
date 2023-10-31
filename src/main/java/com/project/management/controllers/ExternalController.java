package com.project.management.controllers;

import com.project.management.dtos.HardwareInfoDTO;
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
            @RequestBody HardwareInfoDTO hardwareInfoDTO) {
        externalService.updateHardwareValue(
                token,
                hardwareInfoDTO.getGasSensorValue(),
                hardwareInfoDTO.getFlameSensorValue(),
                hardwareInfoDTO.getPressureSensorValue(),
                hardwareInfoDTO.getAmpSensorValue(),
                hardwareInfoDTO.getTemperatureSensorValue(),
                hardwareInfoDTO.getHumiditySensorValue(),
                hardwareInfoDTO.getSecondAmpSensorValue(),
                hardwareInfoDTO.getAcSwitch(),
                hardwareInfoDTO.getAcPumpSwitch(),
                hardwareInfoDTO.getReservedSwitch());
        
        return ResponseDTO
                .builder()
                .message("Success")
                .status(HttpStatus.OK.value())
                .data(null)
                .build();
    }
}
