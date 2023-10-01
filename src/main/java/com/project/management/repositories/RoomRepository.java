package com.project.management.repositories;

import com.project.management.entities.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {
    Boolean existsByName(String roomName);

    Optional<Room> findByToken(String uuid);
}
