package com.project.management.services;

import com.project.management.entities.Hardware;
import com.project.management.entities.Room;
import com.project.management.exception.MyException;
import com.project.management.repositories.HardwareRepository;
import com.project.management.repositories.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class ExternalService {
    @Autowired
    RoomRepository roomRepository;

    @Autowired
    HardwareRepository hardwareRepository;

    public void connectHardware(String token) {
        Room room = roomRepository.findByToken(token)
                .orElseThrow(() -> new MyException(HttpStatus.NOT_FOUND, String.format("Room With Token '%s' Not Found", token)));
        Boolean isConnected = room.getIsUsed();
        if (isConnected) {
            throw new MyException(HttpStatus.CONFLICT, String.format("Room With Token '%s' Was Already Connected", token));
        }
        room.setIsUsed(true);
        roomRepository.save(room);
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
        Room room = roomRepository.findByToken(token)
                .orElseThrow(() -> new MyException(HttpStatus.NOT_FOUND, String.format("Room With Token '%s' Not Found", token)));

        Boolean isConnected = room.getIsUsed();
        if (!isConnected) {
            throw new MyException(HttpStatus.LOCKED, String.format("Room With Token '%s' Isn't Connected", token));
        }

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
        room.getHardwares().add(hardware);
        room.setHardwares(room.getHardwares());
        roomRepository.save(room);
    }
}
