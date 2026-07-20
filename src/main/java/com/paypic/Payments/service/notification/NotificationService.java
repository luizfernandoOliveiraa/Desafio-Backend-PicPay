package com.paypic.Payments.service.notification;

import com.paypic.Payments.dto.user.UserResponseDTO;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class NotificationService {

    private final RestTemplate restTemplate;

    public NotificationService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Async
    public void sendNotification(UserResponseDTO user, String message) {
        String email = user.getEmail();
        // Simulating sending notification
        System.out.println("Enviando notificação para " + email);
        try {
            restTemplate.postForEntity("https://util.devi.tools/api/v1/notify", message, String.class);
            System.out.println("Notificação enviada com sucesso");
        } catch (Exception e) {
            System.err.println("Erro ao enviar notificação: " + e.getMessage());
        }
    }
}