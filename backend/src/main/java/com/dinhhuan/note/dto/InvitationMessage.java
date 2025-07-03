package com.dinhhuan.note.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InvitationMessage {
    String inviterEmail;
    String inviteeEmail;
    String teamspaceName;
    String inviteeUsername; // userId receive
    Long id;
    String status;
}
