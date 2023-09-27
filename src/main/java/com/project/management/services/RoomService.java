package com.project.management.services;

import com.project.management.entities.Room;
import com.project.management.entities.User;
import com.project.management.exception.MyException;
import com.project.management.repositories.RoomRepository;
import com.project.management.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

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
        return room.getUuid();
    }

}
