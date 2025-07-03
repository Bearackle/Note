package com.dinhhuan.note.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SearchBlockObject {
    Long workspaceId;
    String workspaceName;
    Long pageId;
    String pageTitle;
    String blockContent;
    public SearchBlockObject() {}
}
