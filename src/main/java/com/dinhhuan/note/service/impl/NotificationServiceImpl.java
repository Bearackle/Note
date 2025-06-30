package com.dinhhuan.note.service.impl;

import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import com.dinhhuan.note.dto.InvitationMessage;
import com.dinhhuan.note.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
public class NotificationServiceImpl implements NotificationService {
    private Log log = LogFactory.get();
    private final RestTemplate restTemplate = new RestTemplate();
    @Override
    public void sendNotification(InvitationMessage message) {
        String gatewayUrl = "http://localhost:3000/notify";
        log.info("inviteeUsername: {}", message.getInviteeUsername());
        log.info("inviterEmail: {}", message.getInviterEmail());
        log.info("teamspaceName: {}", message.getTeamspaceName());
        log.info("invitationId: {}", message.getId());
        log.info("status: {}", message.getStatus());
        Map<String, Object> payload = Map.of(
                "username", message.getInviteeUsername(),
                "inviterEmail", message.getInviterEmail(),
                "teamspace", message.getTeamspaceName(),
                "invitationId" , message.getId(),
                "status" , message.getStatus()
        );
        log.info("SEND NOTIFICATION", payload);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(payload, headers);
        ResponseEntity<String> response = restTemplate.postForEntity(gatewayUrl, request, String.class);
        System.out.println(response.getBody());
    }
}
