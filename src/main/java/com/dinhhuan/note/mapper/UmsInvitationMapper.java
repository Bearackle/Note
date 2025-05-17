package com.dinhhuan.note.mapper;

import com.dinhhuan.note.model.UmsInvitation;
import com.dinhhuan.note.model.UmsInvitationExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface UmsInvitationMapper {
    long countByExample(UmsInvitationExample example);

    int deleteByExample(UmsInvitationExample example);

    int deleteByPrimaryKey(Long id);

    int insert(UmsInvitation row);

    int insertSelective(UmsInvitation row);

    List<UmsInvitation> selectByExample(UmsInvitationExample example);

    UmsInvitation selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("row") UmsInvitation row, @Param("example") UmsInvitationExample example);

    int updateByExample(@Param("row") UmsInvitation row, @Param("example") UmsInvitationExample example);

    int updateByPrimaryKeySelective(UmsInvitation row);

    int updateByPrimaryKey(UmsInvitation row);
}