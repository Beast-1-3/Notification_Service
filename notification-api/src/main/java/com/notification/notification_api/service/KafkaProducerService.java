package com.notification.notification_api.service;

import com.notification.notification_api.entity.Notification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class KafkaProducerService {
    
    private final KafkaTemplate<String, String> kafkaTemplate;
    private static final String TOPIC = "notification-topic";
    
    public void sendNotification(Notification notification) {
        try {
            log.info("Sending notification ID {} to Kafka topic: {}", 
                    notification.getId(), TOPIC);
            
            kafkaTemplate.send(TOPIC, notification.getId().toString());
            
            log.info("Successfully sent notification ID {} to Kafka", 
                    notification.getId());
        } catch (Exception e) {
            log.error("Failed to send notification ID {} to Kafka: {}", 
                    notification.getId(), e.getMessage());
            throw new RuntimeException("Failed to send notification to Kafka", e);
        }
    }
}