package com.project.management.repositories;

import com.project.management.entities.Room;
import com.project.management.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {
    Boolean existsByName(String roomName);

    Optional<Room> findByApiToken(String token);

    @Query(value = "SELECT r FROM Room r WHERE r.name = :roomName")
    List<Room> findByRoomName(String roomName);

    Optional<Room> findByRegisterToken(String token);

    List<Room> findAllByUser(User user);
}
