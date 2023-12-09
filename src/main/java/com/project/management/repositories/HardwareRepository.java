package com.project.management.repositories;

import com.project.management.dtos.AmpereAndVoltageHistoriesDTO;
import com.project.management.dtos.PowerAndWaterConsumptionHistoriesDTO;
import com.project.management.entities.Hardware;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface HardwareRepository extends JpaRepository<Hardware, Long> {
    String REV_TIME = "from_unixtime(ri.revtstmp/1000)";

    @Query(value = "SELECT " +
            "CASE WHEN :timeType = 'day' THEN HOUR("+REV_TIME+") + 7 " +
            "WHEN :timeType = 'month' THEN DAYOFMONTH("+REV_TIME+") " +
            "WHEN :timeType = 'year' THEN MONTH("+REV_TIME+") " +
            "END AS time," +
            "MAX(aud.total_power_consumption) - MIN(aud.total_power_consumption) AS powerConsumption, " +
            "MAX(aud.total_water_consumption) - MIN(aud.total_water_consumption) AS waterConsumption " +
            "FROM hardware_aud aud " +
            "JOIN room r ON aud.pk = r.hardware_fk " +
            "JOIN revinfo ri ON ri.rev = aud.rev " +
            "WHERE r.pk = :roomPk " +
            "AND (CASE WHEN :timeType = 'year' THEN YEAR("+REV_TIME+") = YEAR(:timeFilter) " +
            "WHEN :timeType = 'month' THEN MONTH("+REV_TIME+") = MONTH(:timeFilter) " +
            "WHEN :timeType = 'day' THEN DATE_FORMAT("+REV_TIME+", '%Y-%m-%d') = DATE_FORMAT(:timeFilter, '%Y-%m-%d') END ) " +
            "GROUP BY time ORDER BY time ",
            nativeQuery = true)
    List<PowerAndWaterConsumptionHistoriesDTO> getPowerAndWaterConsumptionHistoriesByRoomPk(Long roomPk, String timeType, Date timeFilter);

    @Query(value = "SELECT " +
            "aud.ampere_sensor_value AS ampere, " +
            "aud.voltage_sensor_value AS voltage " +
            "FROM hardware_aud aud " +
            "JOIN room r ON aud.pk = r.hardware_fk " +
            "JOIN revinfo ri ON ri.rev = aud.rev " +
            "WHERE r.pk = :roomPk " +
            "AND DATE_FORMAT("+REV_TIME+", '%Y-%m-%d') = DATE_FORMAT(:timeFilter, '%Y-%m-%d') ",
            nativeQuery = true)
    List<AmpereAndVoltageHistoriesDTO> getAmpereAndVoltageHistoriesByRoomPk(Long roomPk, Date timeFilter);
}

