package com.assignment.IoT.platform.consumer;

import com.assignment.IoT.platform.alert.AlertGenerationService;
import com.assignment.IoT.platform.model.SensorData;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class SensorDataProcessingService {

    private final Map<String, List<SensorData>> sensorDataMap;
    private final AlertGenerationService alertGenerationService;

    public SensorDataProcessingService(AlertGenerationService alertGenerationService) {
        this.alertGenerationService = alertGenerationService;
        this.sensorDataMap = new HashMap<>();
    }

    public void processSensorData(SensorData sensorData) {
        System.out.println("In processing service");
        String sensorId = sensorData.getSensorId();

        sensorDataMap.putIfAbsent(sensorId, new ArrayList<>());
        sensorDataMap.get(sensorId).add(sensorData);

        removeOldData(sensorId, sensorData.getTimestamp());

        double averageTemperature = calculateAverageTemperature(sensorId);
        if (averageTemperature > 40.0) {
            alertGenerationService.generateAlert(sensorId, averageTemperature);
        }
    }

    public double calculateAverageTemperature(String sensorId) {
        List<SensorData> sensorDataList = sensorDataMap.get(sensorId);
        if (sensorDataList == null || sensorDataList.isEmpty()) {
            return 0;
        }

        double totalTemperature = 0;
        for (SensorData data : sensorDataList) {
            totalTemperature += data.getTemperature();
        }
        return totalTemperature / sensorDataList.size();
    }

    private void removeOldData(String sensorId, LocalDateTime currentTimestamp) {
        List<SensorData> sensorDataList = sensorDataMap.get(sensorId);
        if (sensorDataList == null) return;

        sensorDataList.removeIf(data -> data.getTimestamp().isBefore(currentTimestamp.minusMinutes(5)));
    }
}
