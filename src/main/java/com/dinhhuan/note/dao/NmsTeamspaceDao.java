package com.dinhhuan.note.dao;

import com.dinhhuan.note.dto.InvitationJoinDto;
import com.dinhhuan.note.dto.TeamspaceParamDto;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface NmsTeamspaceDao {
    List<TeamspaceParamDto> getTeamspaceByUserId(@Param("userId") Long userId);
    List<InvitationJoinDto> getInvitationsPendingByEmail(@Param("email") String email);
}
