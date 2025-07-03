package com.dinhhuan.note.dao;

import com.dinhhuan.note.dto.InvitationJoinDto;
import com.dinhhuan.note.dto.TeamspaceParamDto;
import com.dinhhuan.note.model.NmsWorkspace;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface NmsTeamspaceDao {
    List<TeamspaceParamDto> getTeamspaceByUserId(@Param("userId") Long userId);
    List<InvitationJoinDto> getInvitationsPendingByEmail(@Param("email") String email);
    List<NmsWorkspace> getListWorkspacePrivate(@Param("userId") Long userId);
}
