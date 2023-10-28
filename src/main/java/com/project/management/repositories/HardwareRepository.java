package com.project.management.repositories;

import com.project.management.dtos.HardwareUpdateHistoriesDTO;
import com.project.management.entities.Hardware;
import org.hibernate.query.NativeQuery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.history.RevisionRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HardwareRepository extends JpaRepository<Hardware, Long> {

    @Query(value = "SELECT " +
            "weekday(from_unixtime(ri.revtstmp/1000)) AS dateOfWeek, " +
            "AVG(aud.gas_sensor_value) AS gasSensorValue, " +
            "AVG(aud.flame_sensor_value) AS flameSensorValue, " +
                "AVG(aud.pressure_sensor_value) AS pressureSensorValue, " +
            "AVG(aud.motion_sensor_value) AS motionSensorValue, " +
            "AVG(aud.temperature_sensor_value) AS temperatureSensorValue, " +
            "AVG(aud.humidity_sensor_value) AS humiditySensorValue, " +
            "AVG(aud.second_motion_sensor_value) AS secondMotionSensorValue " +
            "FROM hardware_aud aud " +
            "JOIN room r ON aud.room_fk = r.pk " +
            "JOIN revinfo ri ON ri.rev = aud.rev " +
            "WHERE r.pk = :roomPk AND WEEK(from_unixtime(ri.revtstmp/1000)) = WEEK(NOW()) + :week " +
            "GROUP BY dateOfWeek ORDER BY dateOfWeek ",
            nativeQuery = true)
    List<HardwareUpdateHistoriesDTO> getHardwareUpdateHistoriesByRoomPk(Long roomPk, long week);
}

