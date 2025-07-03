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
        String gatewayUrl = "http://localhost:3001/notify";
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
       // sendMailInvitation(message);
    }

    @Override
    public void sendMailInvitation(InvitationMessage message) {
        String mailUrl = "http://localhost:3001/send-mail";
        String invitationMail = """
                <!DOCTYPE html>
                <html>
                <head>
                  ...
                </head>
                <body>
                  <div class="container">
                    <div class="header">You're Invited!</div>
                    <div class="content">
                      <p><strong>%s</strong> has invited you to join the teamspace:</p>
                      <p style="font-size: 18px; font-weight: bold; color: #4f46e5;">%s</p>
                      <p>Click the button below to accept the invitation and start collaborating with your team.</p>
                      <p style="text-align: center;">
                        <a href="%s" class="button">Accept Invitation</a>
                      </p>
                    </div>
                    <div class="footer">
                      If you did not expect this invitation, you can safely ignore this email.
                    </div>
                  </div>
                </body>
                </html>
                """;
        Map<String, Object> payload = Map.of(
                "subject", "Invitation to MemoHub" ,
                "to", message.getInviteeEmail(),
                "text" , message.getInviterEmail() + " invite you to " + message.getTeamspaceName(),
                "html" ,  String.format(invitationMail, message.getInviterEmail(), message.getTeamspaceName(), "https://note.dinhhuan.id.vn")
        );
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map<String, Object>> request = new HttpEntity<>(payload, headers);
        ResponseEntity<String> response = restTemplate.postForEntity(mailUrl, request, String.class);
        log.info(response.getBody());
    }

    @Override
    public void sendMailOtp(String email, String otp) {
        String mailUrl = "http://localhost:3001/send-mail";
        String template = """
                <!DOCTYPE html>
                <html>
                <head>
                  <meta charset="UTF-8" />
                  <meta name="viewport" content="width=device-width, initial-scale=1.0" />
                  <title>Verification Code</title>
                  <style>
                    body {
                      font-family: Arial, sans-serif;
                      background-color: #f7f7f7;
                      margin: 0;
                      padding: 0;
                    }
                    .container {
                      max-width: 400px;
                      background-color: #ffffff;
                      margin: 40px auto;
                      border-radius: 8px;
                      overflow: hidden;
                      box-shadow: 0 2px 8px rgba(0,0,0,0.1);
                    }
                    .header {
                      background-color: #4f46e5;
                      color: white;
                      text-align: center;
                      padding: 20px;
                      font-size: 18px;
                      font-weight: bold;
                    }
                    .content {
                      padding: 20px;
                      color: #333333;
                      font-size: 16px;
                      line-height: 1.5;
                      text-align: center;
                    }
                    .code-box {
                      display: inline-block;
                      background-color: #f0f0f0;
                      padding: 15px 25px;
                      font-size: 28px;
                      font-weight: bold;
                      letter-spacing: 8px;
                      border-radius: 8px;
                      margin: 20px 0;
                      color: #4f46e5;
                      font-family: 'Courier New', monospace;
                    }
                    .footer {
                      text-align: center;
                      font-size: 12px;
                      color: #888888;
                      padding: 10px;
                    }
                  </style>
                </head>
                <body>
                  <div class="container">
                    <div class="header">Your Verification Code</div>
                    <div class="content">
                      <p>Use the code below to verify your email address:</p>
                      <div class="code-box">{{verificationCode}}</div>
                      <p>This code will expire in 5 minutes. Please do not share it with anyone.</p>
                    </div>
                    <div class="footer">
                      If you did not request this, you can ignore this email.
                    </div>
                  </div>
                </body>
                </html>
                """;
        String htmlContent = template.replace("{{verificationCode}}", otp);
        Map<String, Object> payload = Map.of(
                "subject", "Authentication to MemoHub" ,
                "to", email,
                "text" , "Your OTP is " + otp,
                "html" ,  htmlContent
        );
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map<String, Object>> request = new HttpEntity<>(payload, headers);
        ResponseEntity<String> response = restTemplate.postForEntity(mailUrl, request, String.class);
        log.info(response.getBody());
    }

}
