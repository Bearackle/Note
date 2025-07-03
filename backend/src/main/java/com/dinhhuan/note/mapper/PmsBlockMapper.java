package com.dinhhuan.note.mapper;

import com.dinhhuan.note.model.PmsBlock;
import com.dinhhuan.note.model.PmsBlockExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface PmsBlockMapper {
    long countByExample(PmsBlockExample example);

    int deleteByExample(PmsBlockExample example);

    int deleteByPrimaryKey(Long id);

    int insert(PmsBlock row);

    int insertSelective(PmsBlock row);

    List<PmsBlock> selectByExampleWithBLOBs(PmsBlockExample example);

    List<PmsBlock> selectByExample(PmsBlockExample example);

    PmsBlock selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("row") PmsBlock row, @Param("example") PmsBlockExample example);

    int updateByExampleWithBLOBs(@Param("row") PmsBlock row, @Param("example") PmsBlockExample example);

    int updateByExample(@Param("row") PmsBlock row, @Param("example") PmsBlockExample example);

    int updateByPrimaryKeySelective(PmsBlock row);

    int updateByPrimaryKeyWithBLOBs(PmsBlock row);

    int updateByPrimaryKey(PmsBlock row);
}