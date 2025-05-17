package com.dinhhuan.note.dao;

import org.apache.ibatis.annotations.Param;

public interface PmsBlockOrderDao {
    int changeOrderAfter(@Param("pageId") Long pageId, @Param("orderIndex") Integer orderIndex);
}
