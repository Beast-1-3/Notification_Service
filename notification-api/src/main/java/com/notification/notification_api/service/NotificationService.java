package com.notification.notification_api.service;

import com.notification.notification_api.dto.NotificationRequest;
import com.notification.notification_api.dto.NotificationResponse;
import com.notification.notification_api.entity.Notification;
import com.notification.notification_api.entity.NotificationStatus;
import com.notification.notification_api.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationService {
    
    private final NotificationRepository notificationRepository;
    private final KafkaProducerService kafkaProducerService;
    
    @Transactional
    public NotificationResponse createNotification(NotificationRequest request) {
        log.info("Creating notification for recipient: {}", request.getRecipientEmail());
        
        // Create and save notification
        Notification notification = new Notification();
        notification.setRecipientEmail(request.getRecipientEmail());
        notification.setSubject(request.getSubject());
        notification.setBody(request.getBody());
        notification.setStatus(NotificationStatus.PENDING);
        notification.setCreatedAt(LocalDateTime.now());
        notification.setRetryCount(0);
        
        notification = notificationRepository.save(notification);
        log.info("Notification saved with ID: {}", notification.getId());
        
        // Send to Kafka
        kafkaProducerService.sendNotification(notification);
        
        return mapToResponse(notification);
    }
    
    public NotificationResponse getNotificationById(Long id) {
        log.info("Fetching notification with ID: {}", id);
        Notification notification = notificationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Notification not found with ID: " + id));
        return mapToResponse(notification);
    }
    
    public List<NotificationResponse> getAllNotifications() {
        log.info("Fetching all notifications");
        return notificationRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }
    
    private NotificationResponse mapToResponse(Notification notification) {
        return new NotificationResponse(
                notification.getId(),
                notification.getRecipientEmail(),
                notification.getSubject(),
                notification.getStatus(),
                notification.getCreatedAt(),
                notification.getErrorMessage()
        );
    }
}