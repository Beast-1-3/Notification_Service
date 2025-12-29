package com.notification.notification_api.dto;

import com.notification.notification_api.entity.NotificationStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NotificationResponse {
    private Long id;
    private String recipientEmail;
    private String subject;
    private NotificationStatus status;
    private LocalDateTime createdAt;
    private String errorMessage;
}