package com.dinhhuan.note.service.impl;

import com.dinhhuan.note.dao.PmsBlockOrderDao;
import com.dinhhuan.note.dto.PmsBlockParam;
import com.dinhhuan.note.mapper.PmsBlockMapper;
import com.dinhhuan.note.model.PmsBlock;
import com.dinhhuan.note.model.PmsBlockExample;
import com.dinhhuan.note.service.PmsBlockService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PmsBlockServiceImpl implements PmsBlockService {
    @Autowired
    private PmsBlockMapper pmsBlockMapper;
    @Autowired
    private PmsBlockOrderDao pmsBlockOrderDao;

    @Override
    public int create(PmsBlockParam param) {
        PmsBlock pmsBlock = new PmsBlock();
        BeanUtils.copyProperties(param, pmsBlock);
        return pmsBlockMapper.insert(pmsBlock);
    }

    @Override
    public int update(Long id, PmsBlockParam param) {
        PmsBlock pmsBlock = new PmsBlock();
        BeanUtils.copyProperties(param, pmsBlock);
        pmsBlock.setId(id);
        return pmsBlockMapper.updateByPrimaryKeySelective(pmsBlock);
    }

    @Override
    public List<PmsBlock> list(Long pageId) {
        List<PmsBlock> blocks = new ArrayList<>();
        PmsBlockExample example = new PmsBlockExample();
        PmsBlockExample.Criteria criteria = example.createCriteria();
        criteria.andPageIdEqualTo(pageId);
        example.setOrderByClause("order ASC");
        blocks = pmsBlockMapper.selectByExample(example);
        return blocks;
    }

    @Override
    public int updateOrder(Long blockId, PmsBlockParam param) {
        int count = 0;
        PmsBlock block = new PmsBlock();
        BeanUtils.copyProperties(param, block);
        count += pmsBlockMapper.updateByPrimaryKeySelective(block);
        count += pmsBlockOrderDao.changeOrderAfter(block.getPageId(), block.getOrder());
        return count;
    }

    @Override
    public int delete(Long id) {
        return pmsBlockMapper.deleteByPrimaryKey(id);
    }
}
