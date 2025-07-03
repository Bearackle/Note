package com.dinhhuan.note.mapper;

import com.dinhhuan.note.model.UmsUserRoleRelation;
import com.dinhhuan.note.model.UmsUserRoleRelationExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface UmsUserRoleRelationMapper {
    long countByExample(UmsUserRoleRelationExample example);

    int deleteByExample(UmsUserRoleRelationExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(UmsUserRoleRelation row);

    int insertSelective(UmsUserRoleRelation row);

    List<UmsUserRoleRelation> selectByExample(UmsUserRoleRelationExample example);

    UmsUserRoleRelation selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("row") UmsUserRoleRelation row, @Param("example") UmsUserRoleRelationExample example);

    int updateByExample(@Param("row") UmsUserRoleRelation row, @Param("example") UmsUserRoleRelationExample example);

    int updateByPrimaryKeySelective(UmsUserRoleRelation row);

    int updateByPrimaryKey(UmsUserRoleRelation row);
}