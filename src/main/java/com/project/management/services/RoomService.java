package com.project.management.services;

import com.project.management.dtos.*;
import com.project.management.entities.Hardware;
import com.project.management.entities.HardwareLimit;
import com.project.management.entities.Room;
import com.project.management.entities.User;
import com.project.management.exception.MyException;
import com.project.management.repositories.HardwareRepository;
import com.project.management.repositories.RoomRepository;
import com.project.management.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class RoomService {
    @Autowired
    RoomRepository roomRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    HardwareRepository hardwareRepository;

    public String createNewRoom(String roomName, Long userPk) {
        Boolean isRoomNameExist = roomRepository.existsByName(roomName);
        if (isRoomNameExist) {
            throw new MyException(HttpStatus.CONFLICT, "Room Name Already Exist");
        }

        User user = userRepository.findById(userPk)
                .orElseThrow(() -> new MyException(HttpStatus.NOT_FOUND, String.format("User With Pk = %d Not Found", userPk)));

        Room room = roomRepository.save(new Room(roomName, user));
        return room.getRegisterToken();
    }

    public List<RoomInfoDTO> getAllRooms() {
        List<Room> rooms = roomRepository.findAll();
        return rooms.stream().map(room ->
                RoomInfoDTO
                        .builder()
                        .pk(room.getPk())
                        .token(room.getApiToken())
                        .name(room.getName())
                        .isUsed(room.getIsUsed())
                        .user(UserInfoDTO
                                .builder()
                                .pk(room.getUser().getPk())
                                .createdOn(room.getUser().getCreateOn())
                                .updatedOn(room.getUser().getUpdatedOn())
                                .username(room.getUser().getUsername())
                                .phoneNumber(room.getUser().getPhoneNumber())
                                .fullName(room.getUser().getFullName())
                                .address(room.getUser().getAddress())
                                .email(room.getUser().getEmail())
                                .role(room.getUser().getRoles().get(0).getName())
                                .build())
                        .createdOn(room.getCreateOn())
                        .updatedOn(room.getUpdatedOn())
                        .build())
                .collect(Collectors.toList());
    }

    public List<RoomInfoDTO> getRoomsByUserPk(Long userPk) {
        User user = userRepository.findById(userPk)
                .orElseThrow(() -> new MyException(HttpStatus.NOT_FOUND, String.format("User With Pk = %d Not Found", userPk)));
        List<Room> rooms = roomRepository.findAllByUser(user);
        return rooms.stream().map(room ->
                        RoomInfoDTO
                                .builder()
                                .pk(room.getPk())
                                .token(room.getApiToken())
                                .name(room.getName())
                                .isUsed(room.getIsUsed())
                                .createdOn(room.getCreateOn())
                                .updatedOn(room.getUpdatedOn())
                                .build())
                .collect(Collectors.toList());

    }

    public List<PowerAndWaterConsumptionHistoriesDTO> getPowerAndWaterConsumptionHistoriesByRoomPk(Long roomPk, String timeType, String timeFilter) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        List<PowerAndWaterConsumptionHistoriesDTO> historiesDTOS = new ArrayList<>();

        try {
            Date date = dateFormat.parse(timeFilter);
            historiesDTOS = hardwareRepository
                    .getPowerAndWaterConsumptionHistoriesByRoomPk(roomPk, timeType, date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return historiesDTOS;

    }

    public void updateHardware(Long pk, HardwareRequestDTO requestDTO) {
       Room room = roomRepository.findById(pk)
               .orElseThrow(() -> new MyException(HttpStatus.NOT_FOUND, String.format("Room With Pk '%d' Not Found", pk)));

        Hardware hardware = room.getHardware();
        if (Objects.isNull(hardware)) {
            throw new MyException(HttpStatus.NOT_FOUND, "This Room Hasn't Connect To Hardware Yet");
        }
        hardware.setAcSwitch1(requestDTO.getAcSwitch1());
        hardware.setAcSwitch2(requestDTO.getAcSwitch2());
        hardware.setUserReq(requestDTO.getUserReq());
        hardware.setRebootReq(requestDTO.getRebootReq());
        hardware.setResetFactoryReq(requestDTO.getResetFactoryReq());

        roomRepository.save(room);
    }

    public void deleteRoomByPk(Long pk) {
        Room room = roomRepository.findById(pk)
                .orElseThrow(() -> new MyException(HttpStatus.NOT_FOUND, String.format("Room With Pk '%d' Not Found", pk)));
        Long hardwareId = room.getHardware().getPk();
        roomRepository.deleteById(pk);
        hardwareRepository.deleteById(hardwareId);
        hardwareRepository.deleteAuditByHardwareId(hardwareId);
    }

    public String updateRoomByPk(Long pk, String name) {
        Room room = roomRepository.findById(pk)
                .orElseThrow(() -> new MyException(HttpStatus.NOT_FOUND, String.format("Room With Pk '%d' Not Found", pk)));
        room.setName(name);
        return roomRepository.save(room).getName();
    }

    public void updateHardwareLimit(Long pk, HardwareLimitDTO requestDTO) {
        Room room = roomRepository.findById(pk)
                .orElseThrow(() -> new MyException(HttpStatus.NOT_FOUND, String.format("Room With Pk '%d' Not Found", pk)));

        Hardware hardware = room.getHardware();
        if (Objects.isNull(hardware)) {
            throw new MyException(HttpStatus.NOT_FOUND, "This Room Hasn't Connect To Hardware Yet");
        }

        Optional<HardwareLimit> hardwareLimit = hardware.getLimitList()
                .stream().filter(item -> Objects.equals(item.getHardwareId(), requestDTO.getHardwareId())).findFirst();
        if (hardwareLimit.isPresent()) {
            hardwareLimit.get().setUpperLimit(requestDTO.getUpperLimit());
            hardwareLimit.get().setLowerLimit(requestDTO.getLowerLimit());
        } else {
            HardwareLimit limit = new HardwareLimit();
            limit.setHardwareId(requestDTO.getHardwareId());
            limit.setUpperLimit(requestDTO.getUpperLimit());
            limit.setLowerLimit(requestDTO.getLowerLimit());
            limit.setHardware(hardware);
            hardware.getLimitList().add(limit);
        }

        roomRepository.save(room);

    }

    public void deleteHardwareLimit(Long pk, String hardwareId) {
        Room room = roomRepository.findById(pk)
                .orElseThrow(() -> new MyException(HttpStatus.NOT_FOUND, String.format("Room With Pk '%d' Not Found", pk)));

        Hardware hardware = room.getHardware();
        if (Objects.isNull(hardware)) {
            throw new MyException(HttpStatus.NOT_FOUND, "This Room Hasn't Connect To Hardware Yet");
        }

        Optional<HardwareLimit> hardwareLimit = hardware.getLimitList()
                .stream().filter(item -> Objects.equals(item.getHardwareId(), hardwareId)).findFirst();

        if (hardwareLimit.isPresent()) {
            hardware.getLimitList().removeIf(limit -> Objects.equals(limit.getHardwareId(), hardwareId));
            roomRepository.save(room);
        }
    }

    public List<HardwareLimitDTO> getHardwareLimit(Long pk) {
        Room room = roomRepository.findById(pk)
                .orElseThrow(() -> new MyException(HttpStatus.NOT_FOUND, String.format("Room With Pk '%d' Not Found", pk)));

        Hardware hardware = room.getHardware();
        if (Objects.isNull(hardware)) {
            throw new MyException(HttpStatus.NOT_FOUND, "This Room Hasn't Connect To Hardware Yet");
        }

        return hardware.getLimitList().stream().map(hardwareLimit -> HardwareLimitDTO
                .builder()
                .hardwareId(hardwareLimit.getHardwareId())
                .upperLimit(hardwareLimit.getUpperLimit())
                .lowerLimit(hardwareLimit.getLowerLimit())
                .build()).collect(Collectors.toList());
    }

}
