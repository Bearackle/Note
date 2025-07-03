package com.dinhhuan.note.dao;

import org.apache.ibatis.annotations.Param;

public interface PmsBlockOrderDao {
    void changeOrderAfter(@Param("pageId") Long pageId, @Param("orderIndex") Integer orderIndex);
    void changeOrderAfterRemove(@Param("pageId") Long pageId, @Param("orderIndex") Integer orderIndex);
}
