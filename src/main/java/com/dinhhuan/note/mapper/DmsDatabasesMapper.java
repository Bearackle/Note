package com.dinhhuan.note.mapper;

import com.dinhhuan.note.model.DmsDatabases;
import com.dinhhuan.note.model.DmsDatabasesExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface DmsDatabasesMapper {
    long countByExample(DmsDatabasesExample example);

    int deleteByExample(DmsDatabasesExample example);

    int deleteByPrimaryKey(Long id);

    int insert(DmsDatabases row);

    int insertSelective(DmsDatabases row);

    List<DmsDatabases> selectByExampleWithBLOBs(DmsDatabasesExample example);

    List<DmsDatabases> selectByExample(DmsDatabasesExample example);

    DmsDatabases selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("row") DmsDatabases row, @Param("example") DmsDatabasesExample example);

    int updateByExampleWithBLOBs(@Param("row") DmsDatabases row, @Param("example") DmsDatabasesExample example);

    int updateByExample(@Param("row") DmsDatabases row, @Param("example") DmsDatabasesExample example);

    int updateByPrimaryKeySelective(DmsDatabases row);

    int updateByPrimaryKeyWithBLOBs(DmsDatabases row);

    int updateByPrimaryKey(DmsDatabases row);
}