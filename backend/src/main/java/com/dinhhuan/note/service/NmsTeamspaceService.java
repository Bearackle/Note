package com.dinhhuan.note.service;

import com.dinhhuan.note.dto.TeamspaceParamDto;
import com.dinhhuan.note.dto.WorkspaceParam;
import com.dinhhuan.note.model.NmsWorkspace;

import java.util.List;

public interface NmsTeamspaceService {
    int createTeamspace(WorkspaceParam param);
    List<TeamspaceParamDto> listTeamspace();
    Integer update(Long wid, WorkspaceParam param);
}
