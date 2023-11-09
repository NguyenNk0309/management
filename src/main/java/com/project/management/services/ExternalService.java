package com.project.management.services;

import com.project.management.dtos.HardwareInfoDTO;
import com.project.management.dtos.HardwareRequestDTO;
import com.project.management.entities.Hardware;
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

    public String connectHardware(String token) {
        Room room = roomRepository.findByRegisterToken(token)
                .orElseThrow(() -> new MyException(HttpStatus.NOT_FOUND, String.format("Room With Token '%s' Not Found", token)));
        Boolean isConnected = room.getIsUsed();
        if (isConnected) {
            throw new MyException(HttpStatus.CONFLICT, String.format("Room With Token '%s' Was Already Connected", token));
        }
        room.setIsUsed(true);
        return roomRepository.save(room).getApiToken();
    }
    
    public void updateHardwareValue(String token, 
                                    Integer gasSensorValue, 
                                    Integer flameSensorValue,
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

        if (Objects.isNull(room.getHardware())) {
            Hardware hardware = Hardware
                    .builder()
                    .gasSensorValue(gasSensorValue)
                    .flameSensorValue(flameSensorValue)
                    .pressureSensorValue(pressureSensorValue)
                    .ampSensorValue(ampSensorValue)
                    .temperatureSensorValue(temperatureSensorValue)
                    .humiditySensorValue(humiditySensorValue)
                    .secondAmpSensorValue(secondAmpSensorValue)
                    .powerConsumption(powerConsumption)
                    .waterConsumption(waterConsumption)
                    .acSwitch(acSwitch)
                    .acPumpSwitch(acPumpSwitch)
                    .room(room)
                    .build();
            room.setHardware(hardware);
        } else {
            room.getHardware().setGasSensorValue(gasSensorValue);
            room.getHardware().setFlameSensorValue(flameSensorValue);
            room.getHardware().setPressureSensorValue(pressureSensorValue);
            room.getHardware().setAmpSensorValue(ampSensorValue);
            room.getHardware().setTemperatureSensorValue(temperatureSensorValue);
            room.getHardware().setHumiditySensorValue(humiditySensorValue);
            room.getHardware().setSecondAmpSensorValue(secondAmpSensorValue);
            room.getHardware().setPowerConsumption(powerConsumption);
            room.getHardware().setWaterConsumption(waterConsumption);
            room.getHardware().setAcSwitch(acSwitch);
            room.getHardware().setAcPumpSwitch(acPumpSwitch);
        }

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
