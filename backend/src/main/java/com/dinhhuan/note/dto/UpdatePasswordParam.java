package com.dinhhuan.note.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdatePasswordParam {
    private String username;
    private String oldPassword;
    private String newPassword;
}
