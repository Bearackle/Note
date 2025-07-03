package com.dinhhuan.note.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TeamspaceParamDto {
    Long workspaceId;
    String workspaceName;
    String role;
    String description;
}
