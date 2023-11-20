package com.project.management.controllers;

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
            @RequestParam(name = "V0", defaultValue = "0") Float gasSensorValue,
            @RequestParam(name = "V1", defaultValue = "0") Float voltageSensorValue,
            @RequestParam(name = "V2", defaultValue = "0") Float ampereSensorValue,
            @RequestParam(name = "V3", defaultValue = "0") Float temperatureSensorValue,
            @RequestParam(name = "V4", defaultValue = "0") Float humiditySensorValue,
            @RequestParam(name = "V5", defaultValue = "0") Float waterSensorValue,
            @RequestParam(name = "V6", defaultValue = "0") Float fireSensor1Value,
            @RequestParam(name = "V7", defaultValue = "0") Float fireSensor2Value,
            @RequestParam(name = "V8", defaultValue = "0") Float fireSensor3Value,
            @RequestParam(name = "V9", defaultValue = "0") Float fireSensor4Value,
            @RequestParam(name = "V10", defaultValue = "0") Float fireSensor5Value,
            @RequestParam(name = "V11", defaultValue = "false") Boolean acSwitch1,
            @RequestParam(name = "V12", defaultValue = "false") Boolean acSwitch2,
            @RequestParam(name = "V13", defaultValue = "0") Float totalPowerConsumption,
            @RequestParam(name = "V14", defaultValue = "0") Float totalWaterConsumption
            ) {
        externalService.updateHardwareValue(
                token,
                gasSensorValue,
                voltageSensorValue,
                ampereSensorValue,
                temperatureSensorValue,
                humiditySensorValue,
                waterSensorValue,
                fireSensor1Value,
                fireSensor2Value,
                fireSensor3Value,
                fireSensor4Value,
                fireSensor5Value,
                acSwitch1,
                acSwitch2,
                totalPowerConsumption,
                totalWaterConsumption);
        
        return ResponseDTO
                .builder()
                .message("Success")
                .status(HttpStatus.OK.value())
                .data(null)
                .build();
    }
}
