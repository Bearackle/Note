package com.dinhhuan.note.dao;

import com.dinhhuan.note.dto.SearchBlockObject;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface PmsBlockSearchDao {
    List<SearchBlockObject> search(@Param("keyword") String keyword, @Param("userId") Long userId);
}
