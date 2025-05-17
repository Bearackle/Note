package com.dinhhuan.note.service;

import com.dinhhuan.note.dto.PmsPageParam;
import com.dinhhuan.note.model.PmsPage;

import java.util.List;

public interface PmsPageService {
    int create(PmsPageParam param);
    List<PmsPage> list(Long workspaceId);
    List<PmsPage> getChildPages(Long pageId);
    int update(Long id, PmsPageParam param);
    int deletePage(Long id);
}
