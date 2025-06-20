package com.dinhhuan.note.dao;

import com.dinhhuan.note.dto.PageShareDto;
import org.apache.ibatis.annotations.Param;

public interface PmsPageDao {
    PageShareDto getPage(@Param("pageId") Long pageId);
}
