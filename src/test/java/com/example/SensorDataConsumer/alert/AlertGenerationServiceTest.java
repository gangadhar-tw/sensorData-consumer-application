package com.example.SensorDataConsumer.alert;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class AlertGenerationServiceTest {

    @Mock
    private KafkaTemplate<String, String> kafkaTemplate;

    @InjectMocks
    private AlertGenerationService alertGenerationService;

    private final String ALERT_TOPIC = "SENSOR_ALERTS";

    @Test
    void shouldProduceTheAlertToKafkaWhenGenerateAlertMethodIsCalled() {
        String sensorId = "1";
        double temperature = 42.0;
        alertGenerationService.generateAlert(sensorId, temperature);
        String message = "Temperature too high for sensor ( id = " + sensorId + ", temperature = " + temperature + ")";

        verify(kafkaTemplate, times(1)).send(ALERT_TOPIC, message);
    }
}