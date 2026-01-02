package com.notification.notification_worker.service;

import com.notification.notification_worker.entity.Notification;
import com.notification.notification_worker.entity.NotificationStatus;
import com.notification.notification_worker.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class RetryScheduler {
    
    private final NotificationRepository notificationRepository;
    private final NotificationProcessor notificationProcessor;
    
    @Value("${notification.max-retry-count}")
    private int maxRetryCount;
    
    @Value("${notification.retry-delay-minutes}")
    private int retryDelayMinutes;
    
    // Runs every 2 minutes
    @Scheduled(fixedRate = 120000)
    public void retryFailedNotifications() {
        log.info("Checking for pending notifications to retry...");
        
        // Find all PENDING notifications
        List<Notification> pendingNotifications = 
                notificationRepository.findByStatus(NotificationStatus.PENDING);
        
        LocalDateTime now = LocalDateTime.now();
        
        for (Notification notification : pendingNotifications) {
            // Check if retry count is below max
            if (notification.getRetryCount() >= maxRetryCount) {
                log.warn("Notification ID {} exceeded max retries. Marking as FAILED.", 
                        notification.getId());
                notification.setStatus(NotificationStatus.FAILED);
                notificationRepository.save(notification);
                continue;
            }
            
            // Check if enough time has passed since last attempt
            LocalDateTime lastAttemptTime = notification.getUpdatedAt() != null 
                    ? notification.getUpdatedAt() 
                    : notification.getCreatedAt();
            
            LocalDateTime nextRetryTime = lastAttemptTime.plusMinutes(retryDelayMinutes);
            
            if (now.isAfter(nextRetryTime)) {
                log.info("Retrying notification ID: {} (Attempt {}/{})", 
                        notification.getId(), 
                        notification.getRetryCount() + 1, 
                        maxRetryCount);
                
                try {
                    notificationProcessor.processNotification(notification.getId());
                } catch (Exception e) {
                    log.error("Retry failed for notification ID {}: {}", 
                            notification.getId(), e.getMessage());
                }
            }
        }
        
        log.info("Retry check completed.");
    }
}