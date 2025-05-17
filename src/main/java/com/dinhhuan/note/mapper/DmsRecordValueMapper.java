package com.dinhhuan.note.mapper;

import com.dinhhuan.note.model.DmsRecordValue;
import com.dinhhuan.note.model.DmsRecordValueExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface DmsRecordValueMapper {
    long countByExample(DmsRecordValueExample example);

    int deleteByExample(DmsRecordValueExample example);

    int deleteByPrimaryKey(Long id);

    int insert(DmsRecordValue row);

    int insertSelective(DmsRecordValue row);

    List<DmsRecordValue> selectByExampleWithBLOBs(DmsRecordValueExample example);

    List<DmsRecordValue> selectByExample(DmsRecordValueExample example);

    DmsRecordValue selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("row") DmsRecordValue row, @Param("example") DmsRecordValueExample example);

    int updateByExampleWithBLOBs(@Param("row") DmsRecordValue row, @Param("example") DmsRecordValueExample example);

    int updateByExample(@Param("row") DmsRecordValue row, @Param("example") DmsRecordValueExample example);

    int updateByPrimaryKeySelective(DmsRecordValue row);

    int updateByPrimaryKeyWithBLOBs(DmsRecordValue row);

    int updateByPrimaryKey(DmsRecordValue row);
}