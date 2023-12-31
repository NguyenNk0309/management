package com.project.management.services;

import com.project.management.dtos.HardwareInfoDTO;
import com.project.management.dtos.HardwareLimitDTO;
import com.project.management.dtos.HardwareRequestDTO;
import com.project.management.dtos.NotifyDTO;
import com.project.management.entities.Hardware;
import com.project.management.entities.HardwareLimit;
import com.project.management.entities.Room;
import com.project.management.entities.User;
import com.project.management.exception.MyException;
import com.project.management.repositories.HardwareRepository;
import com.project.management.repositories.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import utils.ManagerUtil;

import java.util.ArrayList;
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
    RoomService roomService;

    public String connectHardware(String token) {
        Room room = roomRepository.findByRegisterToken(token)
                .orElseThrow(() -> new MyException(HttpStatus.NOT_FOUND, String.format("Room With Token '%s' Not Found", token)));
        if (room.getIsDeleted()) {
            throw new MyException(HttpStatus.NOT_FOUND, String.format("Room With Token '%s' Not Found", token));
        }

        Hardware hardware = room.getHardware();
        if (Objects.isNull(hardware)) {
            room.setHardware(Hardware
                            .builder()
                            .gasSensorValue(0F)
                            .voltageSensorValue(0F)
                            .ampereSensorValue(0F)
                            .temperatureSensorValue(0F)
                            .humiditySensorValue(0F)
                            .waterSensorValue(0F)
                            .fireSensor1Value(0F)
                            .fireSensor2Value(0F)
                            .fireSensor3Value(0F)
                            .fireSensor4Value(0F)
                            .fireSensor5Value(0F)
                            .acSwitch1(false)
                            .acSwitch2(false)
                            .userReq(false)
                            .resetFactoryReq(false)
                            .rebootReq(false)
                            .totalPowerConsumption(0F)
                            .totalWaterConsumption(0F)
                            .limitList(new ArrayList<>())
                            .build());
        }
        room.setIsUsed(true);
        Room savedRoom =  roomRepository.save(room);

        for (int i = 6; i <= 10; i++) {
            if (Objects.equals(i, 7) || Objects.equals(i, 9)) {
                roomService.updateHardwareLimit(savedRoom.getPk(),
                        HardwareLimitDTO
                                .builder()
                                .hardwareId("V" + i)
                                .lowerLimit(null)
                                .upperLimit(500F)
                                .build());
            } else {
                roomService.updateHardwareLimit(savedRoom.getPk(),
                        HardwareLimitDTO
                                .builder()
                                .hardwareId("V" + i)
                                .lowerLimit(null)
                                .upperLimit(0F)
                                .build());
            }
        }
        roomService.updateHardwareLimit(savedRoom.getPk(),
                HardwareLimitDTO
                        .builder()
                        .hardwareId("V0")
                        .lowerLimit(null)
                        .upperLimit(2500F)
                        .build());

        return  savedRoom.getApiToken();
    }

    private void checkLimit(HardwareLimit hardwareLimit, User user, Float value, String sensorId, String roomName) {
        String sensorName = ManagerUtil.getSensorNameById(sensorId);

        if (Objects.nonNull(hardwareLimit.getUpperLimit()) && value > hardwareLimit.getUpperLimit()) {
            simpMessagingTemplate
                    .convertAndSend(
                            String.format("/ws/topic/user/%s", user.getPk()),
                            NotifyDTO.builder()
                                    .sensorId(sensorId)
                                    .roomName(roomName)
                                    .title(String.format("Notification For %s In Room Name: %s", sensorName, roomName))
                                    .description(String.format("The Current Value Of %s (%.2f) Is Higher Than Your Upper Limit Setup (%.2f)", sensorName, value, hardwareLimit.getUpperLimit()))
                                    .build());

            simpMessagingTemplate
                    .convertAndSend("/ws/topic/role/ADMIN",
                            NotifyDTO.builder()
                                    .sensorId(sensorId)
                                    .roomName(roomName)
                                    .title(String.format("Notification For %s In Room Name: %s", sensorName, roomName))
                                    .description(String.format("The Current Value Of %s (%.2f) Is Higher Than Your Upper Limit Setup (%.2f)", sensorName, value, hardwareLimit.getUpperLimit()))
                                    .build());


        } else if (Objects.nonNull(hardwareLimit.getLowerLimit()) && value < hardwareLimit.getLowerLimit()) {
            simpMessagingTemplate
                    .convertAndSend(
                            String.format("/ws/topic/user/%s", user.getPk()),
                            NotifyDTO.builder()
                                    .sensorId(sensorId)
                                    .roomName(roomName)
                                    .title(String.format("Notification For %s In Room Name: %s", sensorName, roomName))
                                    .description(String.format("The Current Value Of %s (%.2f) Is Lower Than Your Lower Limit Setup (%.2f)", sensorName, value, hardwareLimit.getLowerLimit()))
                                    .build());

            simpMessagingTemplate
                    .convertAndSend("/ws/topic/role/ADMIN",
                            NotifyDTO.builder()
                                    .sensorId(sensorId)
                                    .roomName(roomName)
                                    .title(String.format("Notification For %s In Room Name: %s", sensorName, roomName))
                                    .description(String.format("The Current Value Of %s (%.2f) Is Lower Than Your Lower Limit Setup (%.2f)", sensorName, value, hardwareLimit.getLowerLimit()))
                                    .build());

        }
    }

    
    public void updateHardwareValue(String token,
                                    Float gasSensorValue,
                                    Float voltageSensorValue,
                                    Float ampereSensorValue,
                                    Float temperatureSensorValue,
                                    Float humiditySensorValue,
                                    Float waterSensorValue,
                                    Float fireSensor1Value,
                                    Float fireSensor2Value,
                                    Float fireSensor3Value,
                                    Float fireSensor4Value,
                                    Float fireSensor5Value,
                                    Boolean acSwitch1,
                                    Boolean acSwitch2,
                                    Float totalPowerConsumption,
                                    Float totalWaterConsumption,
                                    Boolean userReq,
                                    Boolean resetFactoryReq,
                                    Boolean rebootReq) {
        Room room = roomRepository.findByApiToken(token)
                .orElseThrow(() -> new MyException(HttpStatus.NOT_FOUND, String.format("Room With Token '%s' Not Found", token)));

        if (room.getIsDeleted()) {
            throw new MyException(HttpStatus.NOT_FOUND, String.format("Room With Token '%s' Not Found", token));
        }

        Boolean isConnected = room.getIsUsed();
        if (!isConnected) {
            throw new MyException(HttpStatus.LOCKED, String.format("Room With Token '%s' Isn't Connected", token));
        }

        Hardware hardware = room.getHardware();
        hardware.setGasSensorValue(gasSensorValue);
        hardware.setVoltageSensorValue(voltageSensorValue);
        hardware.setAmpereSensorValue(ampereSensorValue);
        hardware.setTemperatureSensorValue(temperatureSensorValue);
        hardware.setHumiditySensorValue(humiditySensorValue);
        hardware.setWaterSensorValue(waterSensorValue);
        hardware.setFireSensor1Value(fireSensor1Value);
        hardware.setFireSensor2Value(fireSensor2Value);
        hardware.setFireSensor3Value(fireSensor3Value);
        hardware.setFireSensor4Value(fireSensor4Value);
        hardware.setFireSensor5Value(fireSensor5Value);
        hardware.setAcSwitch1(acSwitch1);
        hardware.setAcSwitch2(acSwitch2);
        hardware.setUserReq(userReq);
        hardware.setResetFactoryReq(resetFactoryReq);
        hardware.setRebootReq(rebootReq);
        hardware.setTotalPowerConsumption(totalPowerConsumption);
        hardware.setTotalWaterConsumption(totalWaterConsumption);

        hardware.getLimitList().forEach(hardwareLimit -> {
            switch (hardwareLimit.getHardwareId()) {
                case "V0":
                    checkLimit(hardwareLimit, room.getUser(), gasSensorValue, "V0", room.getName());
                    break;
                case "V1":
                    checkLimit(hardwareLimit, room.getUser(), voltageSensorValue, "V1", room.getName());
                    break;
                case "V2":
                    checkLimit(hardwareLimit, room.getUser(), ampereSensorValue, "V2", room.getName());
                    break;
                case "V3":
                    checkLimit(hardwareLimit, room.getUser(), temperatureSensorValue, "V3", room.getName());
                    break;
                case "V4":
                    checkLimit(hardwareLimit, room.getUser(), humiditySensorValue, "V4", room.getName());
                    break;
                case "V5":
                    checkLimit(hardwareLimit, room.getUser(), waterSensorValue, "V5", room.getName());
                    break;
                case "V6":
                    checkLimit(hardwareLimit, room.getUser(), fireSensor1Value, "V6", room.getName());
                    break;
                case "V7":
                    checkLimit(hardwareLimit, room.getUser(), fireSensor2Value, "V7", room.getName());
                    break;
                case "V8":
                    checkLimit(hardwareLimit, room.getUser(), fireSensor3Value, "V8", room.getName());
                    break;
                case "V9":
                    checkLimit(hardwareLimit, room.getUser(), fireSensor4Value, "V9", room.getName());
                    break;
                case "V10":
                    checkLimit(hardwareLimit, room.getUser(), fireSensor5Value, "V10", room.getName());
                    break;
            }
        });

        simpMessagingTemplate
                .convertAndSend(String.format("/ws/topic/%s", room.getApiToken()),
                        HardwareInfoDTO
                                .builder()
                                .gasSensorValue(gasSensorValue)
                                .voltageSensorValue(voltageSensorValue)
                                .ampereSensorValue(ampereSensorValue)
                                .temperatureSensorValue(temperatureSensorValue)
                                .humiditySensorValue(humiditySensorValue)
                                .waterSensorValue(waterSensorValue)
                                .fireSensor1Value(fireSensor1Value)
                                .fireSensor2Value(fireSensor2Value)
                                .fireSensor3Value(fireSensor3Value)
                                .fireSensor4Value(fireSensor4Value)
                                .fireSensor5Value(fireSensor5Value)
                                .acSwitch1(acSwitch1)
                                .acSwitch2(acSwitch2)
                                .userReq(userReq)
                                .resetFactoryReq(resetFactoryReq)
                                .rebootReq(rebootReq)
                                .totalPowerConsumption(totalPowerConsumption)
                                .totalWaterConsumption(totalWaterConsumption)
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

        HardwareRequestDTO requestDTO = HardwareRequestDTO
                .builder()
                .acSwitch1(room.getHardware().getAcSwitch1())
                .acSwitch2(room.getHardware().getAcSwitch2())
                .userReq(room.getHardware().getUserReq())
                .resetFactoryReq(room.getHardware().getResetFactoryReq())
                .rebootReq(room.getHardware().getRebootReq())
                .build();

        Hardware hardware = room.getHardware();
        hardware.setResetFactoryReq(false);
        hardware.setRebootReq(false);
        hardwareRepository.save(hardware);

        return requestDTO;
    }
}
