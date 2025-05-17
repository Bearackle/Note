package com.dinhhuan.note.mapper;

import com.dinhhuan.note.model.NmsWorkspace;
import com.dinhhuan.note.model.NmsWorkspaceExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface NmsWorkspaceMapper {
    long countByExample(NmsWorkspaceExample example);

    int deleteByExample(NmsWorkspaceExample example);

    int deleteByPrimaryKey(Long id);

    int insert(NmsWorkspace row);

    int insertSelective(NmsWorkspace row);

    List<NmsWorkspace> selectByExampleWithBLOBs(NmsWorkspaceExample example);

    List<NmsWorkspace> selectByExample(NmsWorkspaceExample example);

    NmsWorkspace selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("row") NmsWorkspace row, @Param("example") NmsWorkspaceExample example);

    int updateByExampleWithBLOBs(@Param("row") NmsWorkspace row, @Param("example") NmsWorkspaceExample example);

    int updateByExample(@Param("row") NmsWorkspace row, @Param("example") NmsWorkspaceExample example);

    int updateByPrimaryKeySelective(NmsWorkspace row);

    int updateByPrimaryKeyWithBLOBs(NmsWorkspace row);

    int updateByPrimaryKey(NmsWorkspace row);
}