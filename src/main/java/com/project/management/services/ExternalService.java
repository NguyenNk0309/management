package com.project.management.services;

import com.project.management.dtos.HardwareInfoDTO;
import com.project.management.dtos.HardwareRequestDTO;
import com.project.management.entities.Hardware;
import com.project.management.entities.HardwareLimit;
import com.project.management.entities.Room;
import com.project.management.exception.MyException;
import com.project.management.repositories.HardwareRepository;
import com.project.management.repositories.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Objects;

@Service
public class ExternalService {
    @Autowired
    RoomRepository roomRepository;

    @Autowired
    HardwareRepository hardwareRepository;

    @Autowired
    SimpMessagingTemplate simpMessagingTemplate;

    @Autowired
    EmailSenderService emailSenderService;

    public String connectHardware(String token) {
        Room room = roomRepository.findByRegisterToken(token)
                .orElseThrow(() -> new MyException(HttpStatus.NOT_FOUND, String.format("Room With Token '%s' Not Found", token)));
        Boolean isConnected = room.getIsUsed();
        if (isConnected) {
            throw new MyException(HttpStatus.CONFLICT, String.format("Room With Token '%s' Was Already Connected", token));
        }
        room.setIsUsed(true);
        room.setHardware(Hardware.builder().build());
        return roomRepository.save(room).getApiToken();
    }

// TODO Send Mail
    private void checkLimit(HardwareLimit hardwareLimit, String userEmail, Float value) {
        if (Objects.nonNull(hardwareLimit.getUpperLimit()) && value > hardwareLimit.getUpperLimit()) {
            emailSenderService.sendSimpleEmail(userEmail, "Smart Room Warning", "Upper Limit");
        } else if (Objects.nonNull(hardwareLimit.getLowerLimit()) && value < hardwareLimit.getLowerLimit()) {
            emailSenderService.sendSimpleEmail(userEmail, "Smart Room Warning", "Lower Limit");
        }
    }

    
    public void updateHardwareValue(String token, 
                                    Float gasSensorValue,
                                    Float flameSensorValue,
                                    Float pressureSensorValue,
                                    Float ampSensorValue,
                                    Float temperatureSensorValue,
                                    Float humiditySensorValue,
                                    Float secondAmpSensorValue,
                                    Float powerConsumption,
                                    Float waterConsumption,
                                    Boolean acSwitch,
                                    Boolean acPumpSwitch) {
        Room room = roomRepository.findByApiToken(token)
                .orElseThrow(() -> new MyException(HttpStatus.NOT_FOUND, String.format("Room With Token '%s' Not Found", token)));

        Boolean isConnected = room.getIsUsed();
        if (!isConnected) {
            throw new MyException(HttpStatus.LOCKED, String.format("Room With Token '%s' Isn't Connected", token));
        }

        String userEmail = room.getUser().getEmail();

        Hardware hardware = room.getHardware();
        hardware.setGasSensorValue(gasSensorValue);
        hardware.setFlameSensorValue(flameSensorValue);
        hardware.setPressureSensorValue(pressureSensorValue);
        hardware.setAmpSensorValue(ampSensorValue);
        hardware.setTemperatureSensorValue(temperatureSensorValue);
        hardware.setHumiditySensorValue(humiditySensorValue);
        hardware.setSecondAmpSensorValue(secondAmpSensorValue);
        hardware.setPowerConsumption(powerConsumption);
        hardware.setWaterConsumption(waterConsumption);
        hardware.setAcSwitch(acSwitch);
        hardware.setAcPumpSwitch(acPumpSwitch);

        hardware.getLimitList().forEach(hardwareLimit -> {
            switch (hardwareLimit.getHardwareId()) {
                case "V0":
                    checkLimit(hardwareLimit, userEmail, gasSensorValue);
                    break;
                case "V1":
                    checkLimit(hardwareLimit, userEmail, flameSensorValue);
                    break;
                case "V2":
                    checkLimit(hardwareLimit, userEmail, pressureSensorValue);
                    break;
                case "V3":
                    checkLimit(hardwareLimit, userEmail, ampSensorValue);
                    break;
                case "V4":
                    checkLimit(hardwareLimit, userEmail, temperatureSensorValue);
                    break;
                case "V5":
                    checkLimit(hardwareLimit, userEmail, humiditySensorValue);
                    break;
                case "V6":
                    checkLimit(hardwareLimit, userEmail, secondAmpSensorValue);
                    break;
            }
        });

        simpMessagingTemplate
                .convertAndSend(String.format("/ws/topic/%s", room.getApiToken()),
                        HardwareInfoDTO
                                .builder()
                                .gasSensorValue(gasSensorValue)
                                .flameSensorValue(flameSensorValue)
                                .pressureSensorValue(pressureSensorValue)
                                .ampSensorValue(ampSensorValue)
                                .temperatureSensorValue(temperatureSensorValue)
                                .humiditySensorValue(humiditySensorValue)
                                .secondAmpSensorValue(secondAmpSensorValue)
                                .acSwitch(acSwitch)
                                .acPumpSwitch(acPumpSwitch)
                                .updatedOn(new Date())
                                .build());

        roomRepository.save(room);
    }

    public HardwareRequestDTO getRequest(String token) {
        Room room = roomRepository.findByApiToken(token)
                .orElseThrow(() -> new MyException(HttpStatus.NOT_FOUND, String.format("Room With Token '%s' Not Found", token)));

        Boolean isConnected = room.getIsUsed();
        if (!isConnected) {
            throw new MyException(HttpStatus.LOCKED, String.format("Room With Token '%s' Isn't Connected", token));
        }

        return HardwareRequestDTO
                .builder()
                .acPumpSwitch(room.getHardware().getAcPumpSwitch())
                .acSwitch(room.getHardware().getAcSwitch())
                .isShutdown(room.getHardware().getIsShutdown())
                .isReboot(room.getHardware().getIsReboot())
                .build();
    }
}
