package com.project.management.services;

import com.project.management.dtos.HardwareInfoDTO;
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
import org.springframework.web.bind.annotation.RequestParam;
import utils.ManagerUtil;

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
        Hardware hardware = room.getHardware();
        if (Objects.isNull(hardware)) {
            room.setHardware(new Hardware());
        }
        room.setIsUsed(true);
        return roomRepository.save(room).getApiToken();
    }

    private void checkLimit(HardwareLimit hardwareLimit, User user, Float value, String sensorName, String roomName) {
        if (Objects.nonNull(hardwareLimit.getUpperLimit()) && value > hardwareLimit.getUpperLimit()) {
            simpMessagingTemplate
                    .convertAndSend(
                            String.format("/ws/topic/user/%s", user.getPk()),
                            NotifyDTO.builder()
                                    .title(String.format("Notification For %s In Room Name: %s", sensorName, roomName))
                                    .description(String.format("The Current Value Of %s (%.2f) Is Higher Than Your Upper Limit Setup (%.2f)", sensorName, value, hardwareLimit.getUpperLimit()))
                                    .build());

            simpMessagingTemplate
                    .convertAndSend("/ws/topic/role/ADMIN",
                            NotifyDTO.builder()
                                    .title(String.format("Notification For %s In Room Name: %s", sensorName, roomName))
                                    .description(String.format("The Current Value Of %s (%.2f) Is Higher Than Your Upper Limit Setup (%.2f)", sensorName, value, hardwareLimit.getUpperLimit()))
                                    .build());


        } else if (Objects.nonNull(hardwareLimit.getLowerLimit()) && value < hardwareLimit.getLowerLimit()) {
            simpMessagingTemplate
                    .convertAndSend(
                            String.format("/ws/topic/user/%s", user.getPk()),
                            NotifyDTO.builder()
                                    .title(String.format("Notification For %s In Room Name: %s", sensorName, roomName))
                                    .description(String.format("The Current Value Of %s (%.2f) Is Lower Than Your Lower Limit Setup (%.2f)", sensorName, value, hardwareLimit.getLowerLimit()))
                                    .build());

            simpMessagingTemplate
                    .convertAndSend("/ws/topic/role/ADMIN",
                            NotifyDTO.builder()
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
                    checkLimit(hardwareLimit, room.getUser(), gasSensorValue, ManagerUtil.getSensorNameById("V0"), room.getName());
                    break;
                case "V1":
                    checkLimit(hardwareLimit, room.getUser(), voltageSensorValue, ManagerUtil.getSensorNameById("V1"), room.getName());
                    break;
                case "V2":
                    checkLimit(hardwareLimit, room.getUser(), ampereSensorValue, ManagerUtil.getSensorNameById("V2"), room.getName());
                    break;
                case "V3":
                    checkLimit(hardwareLimit, room.getUser(), temperatureSensorValue, ManagerUtil.getSensorNameById("V3"), room.getName());
                    break;
                case "V4":
                    checkLimit(hardwareLimit, room.getUser(), humiditySensorValue, ManagerUtil.getSensorNameById("V4"), room.getName());
                    break;
                case "V5":
                    checkLimit(hardwareLimit, room.getUser(), waterSensorValue, ManagerUtil.getSensorNameById("V5"), room.getName());
                    break;
                case "V6":
                    checkLimit(hardwareLimit, room.getUser(), fireSensor1Value, ManagerUtil.getSensorNameById("V6"), room.getName());
                    break;
                case "V7":
                    checkLimit(hardwareLimit, room.getUser(), fireSensor2Value, ManagerUtil.getSensorNameById("V7"), room.getName());
                    break;
                case "V8":
                    checkLimit(hardwareLimit, room.getUser(), fireSensor3Value, ManagerUtil.getSensorNameById("V8"), room.getName());
                    break;
                case "V9":
                    checkLimit(hardwareLimit, room.getUser(), fireSensor4Value, ManagerUtil.getSensorNameById("V9"), room.getName());
                    break;
                case "V10":
                    checkLimit(hardwareLimit, room.getUser(), fireSensor5Value, ManagerUtil.getSensorNameById("V10"), room.getName());
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

        return HardwareRequestDTO
                .builder()
                .acSwitch1(room.getHardware().getAcSwitch1())
                .acSwitch2(room.getHardware().getAcSwitch2())
                .userReq(room.getHardware().getUserReq())
                .resetFactoryReq(room.getHardware().getResetFactoryReq())
                .rebootReq(room.getHardware().getRebootReq())
                .build();
    }
}
