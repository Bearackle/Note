package com.dinhhuan.note.mapper;

import com.dinhhuan.note.model.UmsUser;
import com.dinhhuan.note.model.UmsUserExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface UmsUserMapper {
    long countByExample(UmsUserExample example);

    int deleteByExample(UmsUserExample example);

    int deleteByPrimaryKey(Long id);

    int insert(UmsUser row);

    int insertSelective(UmsUser row);

    List<UmsUser> selectByExample(UmsUserExample example);

    UmsUser selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("row") UmsUser row, @Param("example") UmsUserExample example);

    int updateByExample(@Param("row") UmsUser row, @Param("example") UmsUserExample example);

    int updateByPrimaryKeySelective(UmsUser row);

    int updateByPrimaryKey(UmsUser row);
}