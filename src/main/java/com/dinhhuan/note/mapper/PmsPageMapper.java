package com.dinhhuan.note.mapper;

import com.dinhhuan.note.model.PmsPage;
import com.dinhhuan.note.model.PmsPageExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface PmsPageMapper {
    long countByExample(PmsPageExample example);

    int deleteByExample(PmsPageExample example);

    int deleteByPrimaryKey(Long id);

    int insert(PmsPage row);

    int insertSelective(PmsPage row);

    List<PmsPage> selectByExample(PmsPageExample example);

    PmsPage selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("row") PmsPage row, @Param("example") PmsPageExample example);

    int updateByExample(@Param("row") PmsPage row, @Param("example") PmsPageExample example);

    int     updateByPrimaryKeySelective(PmsPage row);

    int updateByPrimaryKey(PmsPage row);
}