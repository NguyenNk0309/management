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
            @RequestParam(name = "V0") Integer gasSensorValue,
            @RequestParam(name = "V1") Integer flameSensorValue,
            @RequestParam(name = "V2") Float pressureSensorValue,
            @RequestParam(name = "V3") Float ampSensorValue,
            @RequestParam(name = "V4") Float temperatureSensorValue,
            @RequestParam(name = "V5") Float humiditySensorValue,
            @RequestParam(name = "V6") Float secondAmpSensorValue,
            @RequestParam(name = "V7") Float powerConsumption,
            @RequestParam(name = "V8") Float waterConsumption,
            @RequestParam(name = "V9") Boolean acSwitch,
            @RequestParam(name = "V10") Boolean acPumpSwitch
            ) {
        externalService.updateHardwareValue(
                token,
                gasSensorValue,
                flameSensorValue,
                pressureSensorValue,
                ampSensorValue,
                temperatureSensorValue,
                humiditySensorValue,
                secondAmpSensorValue,
                powerConsumption,
                waterConsumption,
                acSwitch,
                acPumpSwitch);
        
        return ResponseDTO
                .builder()
                .message("Success")
                .status(HttpStatus.OK.value())
                .data(null)
                .build();
    }
}
