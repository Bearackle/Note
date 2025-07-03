package com.dinhhuan.note.dto;

import com.dinhhuan.note.enums.WorkSpaceRole;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TeamspaceInvitationDto {
    Long id;
    String inviteeEmail;
    int objectId;
    String role;
}
