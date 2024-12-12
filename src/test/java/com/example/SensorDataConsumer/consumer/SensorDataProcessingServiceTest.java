package com.example.SensorDataConsumer.consumer;

import com.example.SensorDataConsumer.model.SensorData;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.closeTo;
import static org.hamcrest.Matchers.is;

@ExtendWith(MockitoExtension.class)
class SensorDataProcessingServiceTest {

    @InjectMocks
    private SensorDataProcessingService sensorDataProcessingService;

    @Test
    void shouldCalculateAverageTemperatureForSpecificSensor() {
        SensorData sensorData1 = getSensorData("1", 33.5, 95.0);
        SensorData sensorData2 = getSensorData("1", 32.0, 94.0);
        SensorData sensorData3 = getSensorData("2", 42.5, 89.0);

        sensorDataProcessingService.processSensorData(sensorData1);
        sensorDataProcessingService.processSensorData(sensorData2);
        sensorDataProcessingService.processSensorData(sensorData3);

        double sensor1AvgTemp = sensorDataProcessingService.calculateAverageTemperature("1");
        double sensor2AvgTemp = sensorDataProcessingService.calculateAverageTemperature("2");

        assertThat(sensor1AvgTemp, is(closeTo(32.75, 0.01)));
        assertThat(sensor2AvgTemp, is(closeTo(42.5, 0.01)));
    }

    private SensorData getSensorData(String id, Double temperature, Double battery) {
        return SensorData.builder()
                .sensorId(id)
                .temperature(temperature)
                .latitude(40.7128)
                .longitude(74.0060)
                .batteryPercentage(battery)
                .timestamp(LocalDateTime.now())
                .build();
    }

}