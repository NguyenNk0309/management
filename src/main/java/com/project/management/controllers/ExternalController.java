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

    @GetMapping("connect")
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

    @GetMapping("update")
    @ResponseStatus(HttpStatus.OK)
    public ResponseDTO updateHardwareValue(
            @RequestParam(name = "token") String token,
            @RequestParam(name = "V0", required = false) Integer gasSensorValue,
            @RequestParam(name = "V1", required = false) Integer flameSensorValue,
            @RequestParam(name = "V2", required = false) Float pressureSensorValue,
            @RequestParam(name = "V3", required = false) Float motionSensorValue,
            @RequestParam(name = "V4", required = false) Float temperatureSensorValue,
            @RequestParam(name = "V5", required = false) Float humiditySensorValue,
            @RequestParam(name = "V6", required = false) Float secondMotionSensorValue,
            @RequestParam(name = "V7", required = false) Boolean acSwitch,
            @RequestParam(name = "V8", required = false) Boolean acPumpSwitch,
            @RequestParam(name = "V9", required = false) Boolean reservedSwitch
    ) {
        externalService.updateHardwareValue(
                token,
                gasSensorValue,
                flameSensorValue,
                pressureSensorValue,
                motionSensorValue,
                temperatureSensorValue,
                humiditySensorValue,
                secondMotionSensorValue,
                acSwitch,
                acPumpSwitch,
                reservedSwitch);
        
        return ResponseDTO
                .builder()
                .message("Success")
                .status(HttpStatus.OK.value())
                .data(null)
                .build();
    }
}
