package com.project.management.services;

import com.project.management.dtos.HardwareRequestDTO;
import com.project.management.dtos.HardwareUpdateHistoriesDTO;
import com.project.management.dtos.RoomInfoDTO;
import com.project.management.dtos.UserInfoDTO;
import com.project.management.entities.Hardware;
import com.project.management.entities.Room;
import com.project.management.entities.User;
import com.project.management.exception.MyException;
import com.project.management.repositories.HardwareRepository;
import com.project.management.repositories.RoomRepository;
import com.project.management.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
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

    public List<HardwareUpdateHistoriesDTO> getHardwareHistoriesByRoomPk(Long roomPk, Long week) {
        if (Objects.isNull(week)) {
            week = 0L;
        }

        List<HardwareUpdateHistoriesDTO> historiesDTOS = hardwareRepository.getHardwareUpdateHistoriesByRoomPk(roomPk, week);
        return historiesDTOS;

    }

    public void updateHardware(Long pk, HardwareRequestDTO requestDTO) {
       Room room = roomRepository.findById(pk)
               .orElseThrow(() -> new MyException(HttpStatus.NOT_FOUND, String.format("Room With Pk '%d' Not Found", pk)));

        Hardware hardware = room.getHardware();
        if (Objects.isNull(hardware)) {
            throw new MyException(HttpStatus.NOT_FOUND, "This Room Hasn't Connect To Hardware Yet");
        }
        hardware.setReservedSwitch(requestDTO.getReservedSwitch());
        hardware.setAcSwitch(requestDTO.getAcSwitch());
        hardware.setAcPumpSwitch(requestDTO.getAcPumpSwitch());
        hardware.setIsReboot(requestDTO.getIsReboot());
        hardware.setIsShutdown(requestDTO.getIsShutdown());

        roomRepository.save(room);
    }

}
