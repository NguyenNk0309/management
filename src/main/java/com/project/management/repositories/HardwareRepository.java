package com.project.management.repositories;

import com.project.management.dtos.HardwareUpdateHistoriesDTO;
import com.project.management.dtos.PowerAndWaterConsumptionHistoriesDTO;
import com.project.management.entities.Hardware;
import org.hibernate.query.NativeQuery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.history.RevisionRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface HardwareRepository extends JpaRepository<Hardware, Long> {
    String REV_TIME = "from_unixtime(ri.revtstmp/1000)";


    @Query(value = "SELECT " +
            "weekday("+REV_TIME+") AS dateOfWeek, " +
            "AVG(aud.gas_sensor_value) AS gasSensorValue, " +
            "AVG(aud.flame_sensor_value) AS flameSensorValue, " +
                "AVG(aud.pressure_sensor_value) AS pressureSensorValue, " +
            "AVG(aud.amp_sensor_value) AS ampSensorValue, " +
            "AVG(aud.temperature_sensor_value) AS temperatureSensorValue, " +
            "AVG(aud.humidity_sensor_value) AS humiditySensorValue, " +
            "AVG(aud.second_amp_sensor_value) AS secondAmpSensorValue " +
            "FROM hardware_aud aud " +
            "JOIN room r ON aud.room_fk = r.pk " +
            "JOIN revinfo ri ON ri.rev = aud.rev " +
            "WHERE r.pk = :roomPk AND WEEK("+REV_TIME+") = WEEK(NOW()) + :week " +
            "GROUP BY dateOfWeek ORDER BY dateOfWeek ",
            nativeQuery = true)
    List<HardwareUpdateHistoriesDTO> getHardwareUpdateHistoriesByRoomPk(Long roomPk, long week);

    @Query(value = "SELECT " +
            "CASE WHEN :timeType = 'day' THEN HOUR("+REV_TIME+") + 7 " +
            "WHEN :timeType = 'month' THEN DAYOFMONTH("+REV_TIME+") " +
            "WHEN :timeType = 'year' THEN MONTH("+REV_TIME+") " +
            "END AS time," +
            "MAX(aud.power_consumption) - MIN(aud.power_consumption) AS powerConsumption, " +
            "MAX(aud.water_consumption) - MIN(aud.water_consumption) AS waterConsumption " +
            "FROM hardware_aud aud " +
            "JOIN room r ON aud.room_fk = r.pk " +
            "JOIN revinfo ri ON ri.rev = aud.rev " +
            "WHERE r.pk = :roomPk " +
            "AND (CASE WHEN :timeType = 'year' THEN YEAR("+REV_TIME+") = YEAR(:timeFilter) " +
            "WHEN :timeType = 'month' THEN MONTH("+REV_TIME+") = MONTH(:timeFilter) " +
            "WHEN :timeType = 'day' THEN DATE_FORMAT("+REV_TIME+", '%Y-%m-%d') = DATE_FORMAT(:timeFilter, '%Y-%m-%d') END ) " +
            "GROUP BY time ORDER BY time ",
            nativeQuery = true)
    List<PowerAndWaterConsumptionHistoriesDTO> getPowerAndWaterConsumptionHistoriesByRoomPk(Long roomPk, String timeType, Date timeFilter);
}

