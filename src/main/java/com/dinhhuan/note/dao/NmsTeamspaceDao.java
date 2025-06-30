package com.dinhhuan.note.dao;

import com.dinhhuan.note.dto.TeamspaceParamDto;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface NmsTeamspaceDao {
    List<TeamspaceParamDto> getTeamspaceByUserId(@Param("userId") Long userId);
}
