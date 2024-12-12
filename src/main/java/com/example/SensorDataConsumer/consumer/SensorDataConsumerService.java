package com.example.SensorDataConsumer.consumer;

import com.example.SensorDataConsumer.model.SensorData;
import org.springframework.stereotype.Service;

@Service
public class SensorDataConsumerService {

    private final SensorDataProcessingService processingService;

    public SensorDataConsumerService(SensorDataProcessingService processingService) {
        this.processingService = processingService;
    }

    public void consumeSensorData(SensorData sensorData) {
        processingService.processSensorData(sensorData);
    }
}
