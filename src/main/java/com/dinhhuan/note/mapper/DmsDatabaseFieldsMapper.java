package com.dinhhuan.note.mapper;

import com.dinhhuan.note.model.DmsDatabaseFields;
import com.dinhhuan.note.model.DmsDatabaseFieldsExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface DmsDatabaseFieldsMapper {
    long countByExample(DmsDatabaseFieldsExample example);

    int deleteByExample(DmsDatabaseFieldsExample example);

    int deleteByPrimaryKey(Long id);

    int insert(DmsDatabaseFields row);

    int insertSelective(DmsDatabaseFields row);

    List<DmsDatabaseFields> selectByExample(DmsDatabaseFieldsExample example);

    DmsDatabaseFields selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("row") DmsDatabaseFields row, @Param("example") DmsDatabaseFieldsExample example);

    int updateByExample(@Param("row") DmsDatabaseFields row, @Param("example") DmsDatabaseFieldsExample example);

    int updateByPrimaryKeySelective(DmsDatabaseFields row);

    int updateByPrimaryKey(DmsDatabaseFields row);
}