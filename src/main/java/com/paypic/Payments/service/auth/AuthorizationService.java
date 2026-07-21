package com.paypic.Payments.service.auth;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
public class AuthorizationService {

    private final RestTemplate restTemplate;

    public AuthorizationService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public boolean isAuthorized() {
        // ResponseEntity<Map> response = restTemplate.getForEntity("https://util.devi.tools/api/v2/authorize", Map.class);
        // if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
        //     String message = (String) response.getBody().get("message");
        //     return "Autorizado".equalsIgnoreCase(message);
        // }
        // return false;
        return true;
    }
}