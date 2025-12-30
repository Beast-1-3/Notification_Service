package com.notification.notification_api.controller;

import com.notification.notification_api.dto.NotificationRequest;
import com.notification.notification_api.dto.NotificationResponse;
import com.notification.notification_api.service.NotificationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
@Slf4j
public class NotificationController {
    
    private final NotificationService notificationService;
    
    @PostMapping
    public ResponseEntity<NotificationResponse> createNotification(
            @Valid @RequestBody NotificationRequest request) {
        log.info("Received request to create notification for: {}", 
                request.getRecipientEmail());
        
        NotificationResponse response = notificationService.createNotification(request);
        
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<NotificationResponse> getNotification(@PathVariable Long id) {
        log.info("Received request to get notification with ID: {}", id);
        
        NotificationResponse response = notificationService.getNotificationById(id);
        
        return ResponseEntity.ok(response);
    }
    
    @GetMapping
    public ResponseEntity<List<NotificationResponse>> getAllNotifications() {
        log.info("Received request to get all notifications");
        
        List<NotificationResponse> responses = notificationService.getAllNotifications();
        
        return ResponseEntity.ok(responses);
    }
}