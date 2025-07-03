package com.dinhhuan.note.enums;

import lombok.Getter;

@Getter
public enum WorkSpaceRole {
    OWNER("owner"),
    TEAMSPACE_OWNER("teamspace_owner"),
    TEAMSPACE_MEMBER("teamspace_member"),;
    private final String role;
    WorkSpaceRole(String role) {
        this.role = role;
    }
    public static WorkSpaceRole fromValue(String value){
        for(WorkSpaceRole wsr : WorkSpaceRole.values()){
            if(wsr.role.equals(value)){
                return wsr;
            }
        }
        throw new IllegalArgumentException("Invalid WorkSpaceRole value: " + value);
    }
}
