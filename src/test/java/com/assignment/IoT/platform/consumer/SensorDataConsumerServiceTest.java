package com.assignment.IoT.platform.consumer;

import com.assignment.IoT.platform.model.SensorData;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SensorDataConsumerServiceTest {
    @InjectMocks
    private SensorDataConsumerService consumerService;

    @Mock
    private SensorDataProcessingService processingService;

    @Test
    void shouldForwardSensorDataToProcessingServiceWhenSensorDataIsConsumed() {
        SensorData sensorData = getSensorData();

        consumerService.consumeSensorData(sensorData);

        verify(processingService, times(1)).processSensorData(sensorData);
    }

    private SensorData getSensorData() {
        return SensorData.builder()
                .sensorId("1")
                .temperature(25.5)
                .latitude(40.7128)
                .longitude(74.0060)
                .batteryPercentage(95.0)
                .timestamp(LocalDateTime.now())
                .build();
    }
}
