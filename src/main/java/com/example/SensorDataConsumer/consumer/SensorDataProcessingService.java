package com.example.SensorDataConsumer.consumer;

import com.example.SensorDataConsumer.model.SensorData;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SensorDataProcessingService {

    private final Map<String, List<SensorData>> sensorDataMap;

    public SensorDataProcessingService() {
        this.sensorDataMap = new HashMap<>();
    }

    public void processSensorData(SensorData sensorData) {
        String sensorId = sensorData.getSensorId();

        sensorDataMap.putIfAbsent(sensorId, new ArrayList<>());
        sensorDataMap.get(sensorId).add(sensorData);

        double averageTemperature = calculateAverageTemperature(sensorId);
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
}
