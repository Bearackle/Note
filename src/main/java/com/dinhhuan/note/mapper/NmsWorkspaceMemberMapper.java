package com.dinhhuan.note.mapper;

import com.dinhhuan.note.model.NmsWorkspaceMember;
import com.dinhhuan.note.model.NmsWorkspaceMemberExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface NmsWorkspaceMemberMapper {
    long countByExample(NmsWorkspaceMemberExample example);

    int deleteByExample(NmsWorkspaceMemberExample example);

    int deleteByPrimaryKey(Long id);

    int insert(NmsWorkspaceMember row);

    int insertSelective(NmsWorkspaceMember row);

    List<NmsWorkspaceMember> selectByExample(NmsWorkspaceMemberExample example);

    NmsWorkspaceMember selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("row") NmsWorkspaceMember row, @Param("example") NmsWorkspaceMemberExample example);

    int updateByExample(@Param("row") NmsWorkspaceMember row, @Param("example") NmsWorkspaceMemberExample example);

    int updateByPrimaryKeySelective(NmsWorkspaceMember row);

    int updateByPrimaryKey(NmsWorkspaceMember row);
}