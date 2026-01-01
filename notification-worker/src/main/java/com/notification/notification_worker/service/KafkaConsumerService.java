package com.notification.notification_worker.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class KafkaConsumerService {
    
    private final NotificationProcessor notificationProcessor;
    
    @KafkaListener(
            topics = "notification-topic",
            groupId = "notification-worker-group"
    )
    public void consumeNotification(String notificationIdStr) {
        log.info("Received notification ID from Kafka: {}", notificationIdStr);
        
        try {
            // Convert String to Long
            Long notificationId = Long.parseLong(notificationIdStr);
            notificationProcessor.processNotification(notificationId);
            log.info("Successfully processed notification ID: {}", notificationId);
        } catch (NumberFormatException e) {
            log.error("Invalid notification ID format: {}", notificationIdStr, e);
        } catch (Exception e) {
            log.error("Error processing notification ID {}: {}", 
                    notificationIdStr, e.getMessage(), e);
        }
    }
}