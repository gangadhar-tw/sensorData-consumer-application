package com.example.SensorDataConsumer.alert;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class AlertGenerationService {

    private final String ALERT_TOPIC = "SENSOR_ALERTS";
    private final KafkaTemplate<String, String> kafkaTemplate;

    public AlertGenerationService(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void generateAlert(String sensorId, double temperature) {
        String message = "Temperature too high for sensor ( id = " + sensorId + ", temperature = " + temperature + ")";
        kafkaTemplate.send(ALERT_TOPIC, message);
    }
}
