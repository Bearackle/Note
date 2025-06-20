package com.dinhhuan.note.service.impl;

import com.dinhhuan.note.dao.PmsBlockOrderDao;
import com.dinhhuan.note.dto.PmsBlockParam;
import com.dinhhuan.note.mapper.PmsBlockMapper;
import com.dinhhuan.note.model.PmsBlock;
import com.dinhhuan.note.model.PmsBlockExample;
import com.dinhhuan.note.service.PmsBlockService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PmsBlockServiceImpl implements PmsBlockService {
    private final Logger log = LoggerFactory.getLogger(PmsBlockServiceImpl.class);
    @Autowired
    private PmsBlockMapper pmsBlockMapper;
    @Autowired
    private PmsBlockOrderDao pmsBlockOrderDao;

    @Override
    public int create(PmsBlockParam param) {
        PmsBlock pmsBlock = new PmsBlock();
        BeanUtils.copyProperties(param, pmsBlock);
        log.info("data pms block input: {}", pmsBlock);
        pmsBlockOrderDao.changeOrderAfter(pmsBlock.getPageId(), pmsBlock.getBlockOrder());
        return pmsBlockMapper.insertSelective(pmsBlock);
    }

    @Override
    public int update(String idBlock, PmsBlockParam param) {
        log.info("data param: {}", param);
        PmsBlock pmsBlock = new PmsBlock();
        BeanUtils.copyProperties(param, pmsBlock);
        PmsBlockExample example = new PmsBlockExample();
        PmsBlockExample.Criteria criteria = example.createCriteria();
        criteria.andIdBlockEqualTo(idBlock);
        criteria.andPageIdEqualTo(pmsBlock.getPageId());
        log.info("data: {}", pmsBlock);
        return pmsBlockMapper.updateByExampleSelective(pmsBlock, example);
    }

    @Override
    public List<PmsBlock> list(Long pageId) {
        List<PmsBlock> blocks = new ArrayList<>();
        PmsBlockExample example = new PmsBlockExample();
        PmsBlockExample.Criteria criteria = example.createCriteria();
        criteria.andPageIdEqualTo(pageId);
        example.setOrderByClause("block_order ASC");
        blocks = pmsBlockMapper.selectByExampleWithBLOBs(example);
        return blocks;
    }

    @Override
    public int updateOrder(Long blockId, PmsBlockParam param) {
        int count = 0;
        PmsBlock block = new PmsBlock();
        BeanUtils.copyProperties(param, block);
        count += pmsBlockMapper.updateByPrimaryKeySelective(block);
        pmsBlockOrderDao.changeOrderAfter(block.getPageId(), block.getBlockOrder());
        return count;
    }

    @Override
    public int delete(String id) {
        PmsBlockExample example = new PmsBlockExample();
        PmsBlockExample.Criteria criteria = example.createCriteria();
        criteria.andIdBlockEqualTo(id);
        PmsBlock pmsBlock = pmsBlockMapper.selectByExample(example).get(0);
        int count = pmsBlockMapper.deleteByExample(example);
        pmsBlockOrderDao.changeOrderAfterRemove(pmsBlock.getPageId(), pmsBlock.getBlockOrder());
        return count;
    }
}
