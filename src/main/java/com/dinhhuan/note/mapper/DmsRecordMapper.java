package com.dinhhuan.note.mapper;

import com.dinhhuan.note.model.DmsRecord;
import com.dinhhuan.note.model.DmsRecordExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface DmsRecordMapper {
    long countByExample(DmsRecordExample example);

    int deleteByExample(DmsRecordExample example);

    int deleteByPrimaryKey(Long id);

    int insert(DmsRecord row);

    int insertSelective(DmsRecord row);

    List<DmsRecord> selectByExample(DmsRecordExample example);

    DmsRecord selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("row") DmsRecord row, @Param("example") DmsRecordExample example);

    int updateByExample(@Param("row") DmsRecord row, @Param("example") DmsRecordExample example);

    int updateByPrimaryKeySelective(DmsRecord row);

    int updateByPrimaryKey(DmsRecord row);
}