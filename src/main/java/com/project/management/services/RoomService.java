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
import org.springframework.orm.hibernate5.HibernateTemplate;
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

    public List<HardwareUpdateHistoriesDTO> getHardwareHistoriesByRoomPk(Long roomPk, Long week) {
        if (Objects.isNull(week)) {
            week = 0L;
        }

        List<HardwareUpdateHistoriesDTO> historiesDTOS = hardwareRepository.getHardwareUpdateHistoriesByRoomPk(roomPk, week);
        return historiesDTOS;

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
        hardware.setAcSwitch(requestDTO.getAcSwitch());
        hardware.setAcPumpSwitch(requestDTO.getAcPumpSwitch());
        hardware.setIsReboot(requestDTO.getIsReboot());
        hardware.setIsShutdown(requestDTO.getIsShutdown());

        roomRepository.save(room);
    }

    public void deleteRoomByPk(Long pk) {
        Room room = roomRepository.findById(pk)
                .orElseThrow(() -> new MyException(HttpStatus.NOT_FOUND, String.format("Room With Pk '%d' Not Found", pk)));
        roomRepository.deleteById(pk);
    }

    public void updateHardwareLimit(Long pk, UpdateHardwareLimitDTO requestDTO) {
        Room room = roomRepository.findById(pk)
                .orElseThrow(() -> new MyException(HttpStatus.NOT_FOUND, String.format("Room With Pk '%d' Not Found", pk)));

        Hardware hardware = room.getHardware();
        if (Objects.isNull(hardware)) {
            throw new MyException(HttpStatus.NOT_FOUND, "This Room Hasn't Connect To Hardware Yet");
        }

        if (Objects.isNull(requestDTO.getUpperLimit()) && Objects.isNull(requestDTO.getLowerLimit())) {
            throw new MyException(HttpStatus.BAD_REQUEST, "Please Provide At Least Limit Value");
        }

        Optional<HardwareLimit> hardwareLimit = hardware.getLimitList()
                .stream().filter(item -> Objects.equals(item.getHardwareId(), requestDTO.getHardwareId())).findFirst();
        if (hardwareLimit.isPresent()) {
            hardwareLimit.get().setUpperLimit(requestDTO.getUpperLimit());
            hardwareLimit.get().setLowerLimit(requestDTO.getLowerLimit());
        } else {
            hardware.getLimitList().add(HardwareLimit
                            .builder()
                            .hardwareId(requestDTO.getHardwareId())
                            .upperLimit(requestDTO.getUpperLimit())
                            .lowerLimit(requestDTO.getLowerLimit())
                            .build());
        }

        hardwareRepository.save(hardware);

    }

}
