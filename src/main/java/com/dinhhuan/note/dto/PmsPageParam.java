package com.dinhhuan.note.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class PmsPageParam {
    Long workspaceId;
    Long parentId;
    String title;
    Long creatorId;
}
