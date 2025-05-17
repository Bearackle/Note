package com.dinhhuan.note.service;

import com.dinhhuan.note.dto.PmsBlockParam;
import com.dinhhuan.note.model.PmsBlock;

import java.util.List;

public interface PmsBlockService {
    int create(PmsBlockParam param);
    int update(Long id,PmsBlockParam param);
    List<PmsBlock> list(Long pageId);
    int updateOrder(Long blockId, PmsBlockParam param);
    int delete(Long id);
}
