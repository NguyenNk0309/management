package com.project.management.services;

import com.project.management.dtos.RoomInfoDTO;
import com.project.management.dtos.UserInfoDTO;
import com.project.management.entities.Room;
import com.project.management.entities.User;
import com.project.management.exception.MyException;
import com.project.management.repositories.RoomRepository;
import com.project.management.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RoomService {
    @Autowired
    RoomRepository roomRepository;

    @Autowired
    UserRepository userRepository;

    public String createNewRoom(String roomName, Long userPk) {
        Boolean isRoomNameExist = roomRepository.existsByName(roomName);
        if (isRoomNameExist) {
            throw new MyException(HttpStatus.CONFLICT, "Room Name Already Exist");
        }

        User user = userRepository.findById(userPk)
                .orElseThrow(() -> new MyException(HttpStatus.NOT_FOUND, String.format("User With Pk = %d Not Found", userPk)));

        Room room = roomRepository.save(new Room(roomName, user));
        return room.getToken();
    }

    public List<RoomInfoDTO> getAllRooms() {
        List<Room> rooms = roomRepository.findAll();
        return rooms.stream().map(room ->
                RoomInfoDTO
                        .builder()
                        .pk(room.getPk())
                        .token(room.getToken())
                        .name(room.getName())
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

}
