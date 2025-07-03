package com.dinhhuan.note.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class WorkspaceParam {
    String name;
    String description;
    Long creatorId;
}
