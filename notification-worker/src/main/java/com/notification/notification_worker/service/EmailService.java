package com.notification.notification_worker.service;

import com.notification.notification_worker.entity.Notification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {
    
    private final JavaMailSender mailSender;
    
    public void sendEmail(Notification notification) {
        try {
            log.info("Sending email to: {}", notification.getRecipientEmail());
            
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(notification.getRecipientEmail());
            message.setSubject(notification.getSubject());
            message.setText(notification.getBody());
            
            mailSender.send(message);
            
            log.info("Email sent successfully to: {}", notification.getRecipientEmail());
            
        } catch (Exception e) {
            log.error("Failed to send email to: {}. Error: {}", 
                    notification.getRecipientEmail(), e.getMessage());
            throw new RuntimeException("Failed to send email: " + e.getMessage(), e);
        }
    }
}