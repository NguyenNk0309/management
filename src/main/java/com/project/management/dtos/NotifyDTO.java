package com.project.management.dtos;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class NotifyDTO {
    private String sensorId;
    private String title;
    private String description;
    private String roomName;

}
