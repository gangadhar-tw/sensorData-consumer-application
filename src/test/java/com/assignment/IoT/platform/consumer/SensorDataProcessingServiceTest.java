package com.assignment.IoT.platform.consumer;

import com.assignment.IoT.platform.alert.AlertGenerationService;
import com.assignment.IoT.platform.model.SensorData;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.closeTo;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class SensorDataProcessingServiceTest {

    @InjectMocks
    private SensorDataProcessingService sensorDataProcessingService;

    @Mock
    private AlertGenerationService alertGenerationService;

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

    @Test
    void shouldReturnZeroAverageTemperatureWhenSensorDataForThatSensorDoesNotExist() {
        double sensor1AvgTemp = sensorDataProcessingService.calculateAverageTemperature("1");

        assertThat(sensor1AvgTemp, is(closeTo(0, 0.01)));
    }

    @Test
    void shouldRemoveOldDataForSpecificSensorAfterFiveMinutesAndConsiderOnlyNewLastFiveMinutesDataForAverageTemperatureCalculation() {
        SensorData oldData = getSensorData("sensor 1", 28.4, 85.0);
        oldData.setTimestamp(LocalDateTime.now().minusMinutes(6));
        SensorData newData = getSensorData("sensor 1", 25.8, 83.0);
        SensorData newDataForSensor2 = getSensorData("sensor 2", 37.1, 34.0);

        sensorDataProcessingService.processSensorData(oldData);
        sensorDataProcessingService.processSensorData(newData);
        sensorDataProcessingService.processSensorData(newDataForSensor2);

        double sensor1AvgTemp = sensorDataProcessingService.calculateAverageTemperature("sensor 1");

        assertThat(sensor1AvgTemp, is(closeTo(25.8, 0.01)));
    }

    @Test
    void shouldGenerateAlertWhenAverageTemperatureExceedsFortyDegrees() {
        SensorData sensorData1 = getSensorData("1", 36.0, 95.0);
        SensorData sensorData2 = getSensorData("1", 45.0, 94.0);

        sensorDataProcessingService.processSensorData(sensorData1);
        sensorDataProcessingService.processSensorData(sensorData2);

        verify(alertGenerationService, times(1)).generateAlert(anyString(), anyDouble());
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