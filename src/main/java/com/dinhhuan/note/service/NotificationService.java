package com.dinhhuan.note.service;

import com.dinhhuan.note.dto.InvitationMessage;

public interface NotificationService {
    void sendNotification(InvitationMessage message);
    void sendMailInvitation(InvitationMessage message);
    void sendMailOtp(String email, String otp);
}
