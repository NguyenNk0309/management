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
                                    Float motionSensorValue,
                                    Float temperatureSensorValue,
                                    Float humiditySensorValue,
                                    Float secondMotionSensorValue,
                                    Boolean acSwitch,
                                    Boolean acPumpSwitch,
                                    Boolean reservedSwitch) {
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
                    .motionSensorValue(motionSensorValue)
                    .temperatureSensorValue(temperatureSensorValue)
                    .humiditySensorValue(humiditySensorValue)
                    .secondMotionSensorValue(secondMotionSensorValue)
                    .acSwitch(acSwitch)
                    .acPumpSwitch(acPumpSwitch)
                    .reservedSwitch(reservedSwitch)
                    .room(room)
                    .build();
            room.setHardware(hardware);
        } else {
            room.getHardware().setGasSensorValue(gasSensorValue);
            room.getHardware().setFlameSensorValue(flameSensorValue);
            room.getHardware().setPressureSensorValue(pressureSensorValue);
            room.getHardware().setMotionSensorValue(motionSensorValue);
            room.getHardware().setTemperatureSensorValue(temperatureSensorValue);
            room.getHardware().setHumiditySensorValue(humiditySensorValue);
            room.getHardware().setSecondMotionSensorValue(secondMotionSensorValue);
            room.getHardware().setAcSwitch(acSwitch);
            room.getHardware().setAcPumpSwitch(acPumpSwitch);
            room.getHardware().setReservedSwitch(reservedSwitch);
        }

        simpMessagingTemplate
                .convertAndSend(String.format("/ws/topic/%s", room.getApiToken()),
                        HardwareInfoDTO
                                .builder()
                                .gasSensorValue(gasSensorValue)
                                .flameSensorValue(flameSensorValue)
                                .pressureSensorValue(pressureSensorValue)
                                .motionSensorValue(motionSensorValue)
                                .temperatureSensorValue(temperatureSensorValue)
                                .humiditySensorValue(humiditySensorValue)
                                .secondMotionSensorValue(secondMotionSensorValue)
                                .acSwitch(acSwitch)
                                .acPumpSwitch(acPumpSwitch)
                                .reservedSwitch(reservedSwitch)
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
                .reservedSwitch(room.getHardware().getReservedSwitch())
                .isShutdown(false) // TODO
                .isReboot(false)
                .build();
    }
}
