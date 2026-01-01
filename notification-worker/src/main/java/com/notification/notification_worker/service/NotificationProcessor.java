package com.notification.notification_worker.service;

import com.notification.notification_worker.entity.Notification;
import com.notification.notification_worker.entity.NotificationStatus;
import com.notification.notification_worker.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationProcessor {
    
    private final NotificationRepository notificationRepository;
    private final EmailService emailService;
    
    @Value("${notification.max-retry-count}")
    private int maxRetryCount;
    
    @Transactional
    public void processNotification(Long notificationId) {
        log.info("Processing notification ID: {}", notificationId);
        
        // Fetch notification from database
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new RuntimeException("Notification not found with ID: " + notificationId));
        
        // Check if already processed
        if (notification.getStatus() == NotificationStatus.SENT || 
            notification.getStatus() == NotificationStatus.DELIVERED) {
            log.info("Notification ID {} already processed. Skipping.", notificationId);
            return;
        }
        
        try {
            // Send email
            emailService.sendEmail(notification);
            
            // Update status to SENT
            notification.setStatus(NotificationStatus.SENT);
            notification.setSentAt(LocalDateTime.now());
            notification.setUpdatedAt(LocalDateTime.now());
            notification.setErrorMessage(null);
            
            notificationRepository.save(notification);
            
            log.info("Notification ID {} sent successfully", notificationId);
            
        } catch (Exception e) {
            log.error("Failed to send notification ID {}: {}", notificationId, e.getMessage());
            
            // Increment retry count
            notification.setRetryCount(notification.getRetryCount() + 1);
            notification.setErrorMessage(e.getMessage());
            notification.setUpdatedAt(LocalDateTime.now());
            
            // Check if max retries reached
            if (notification.getRetryCount() >= maxRetryCount) {
                notification.setStatus(NotificationStatus.FAILED);
                log.error("Notification ID {} failed after {} retries", 
                        notificationId, maxRetryCount);
            }
            
            notificationRepository.save(notification);
            
            // Re-throw to trigger retry mechanism
            throw new RuntimeException("Failed to process notification", e);
        }
    }
}