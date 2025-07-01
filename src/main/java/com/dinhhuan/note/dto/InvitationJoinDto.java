package com.dinhhuan.note.dto;

import com.dinhhuan.note.model.UmsInvitation;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InvitationJoinDto extends UmsInvitation {
    String inviterEmail;
}
