package com.assignment.IoT.platform.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SensorData {
    private String id;
    private String sensorId;
    private Double temperature;
    private Double latitude;
    private Double longitude;
    private Double batteryPercentage;
    private LocalDateTime timestamp;
}
