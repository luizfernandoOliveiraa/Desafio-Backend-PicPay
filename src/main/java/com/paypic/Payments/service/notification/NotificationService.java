package com.paypic.Payments.service.notification;

import com.paypic.Payments.dto.user.UserResponseDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
public class NotificationService {

    private final RestTemplate restTemplate;

    public NotificationService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Async
    public void sendNotification(UserResponseDTO user, String message) {
        String email = user.getEmail();
        log.info("Iniciando envio de notificação assíncrona para: {}", email);
        try {
            restTemplate.postForEntity("https://util.devi.tools/api/v1/notify", message, String.class);
            log.info("Notificação enviada com sucesso para: {}", email);
        } catch (Exception e) {
            log.error("Falha ao enviar notificação para {}: {}", email, e.getMessage());
        }
    }
}