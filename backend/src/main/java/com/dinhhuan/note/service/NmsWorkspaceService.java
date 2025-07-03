package com.dinhhuan.note.service;

import com.dinhhuan.note.dto.WorkspaceParam;
import com.dinhhuan.note.model.NmsWorkspace;

import java.util.List;

public interface NmsWorkspaceService {
    int createWorkspace(WorkspaceParam param);
    NmsWorkspace getWorkspaceInfo(Long id);
    List<NmsWorkspace> listWorkspaces();
    Integer delete(Long id);
}
