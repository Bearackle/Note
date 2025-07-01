package com.dinhhuan.note.service.impl;

import com.dinhhuan.note.dao.PmsPageDao;
import com.dinhhuan.note.dto.PagePermissionDto;
import com.dinhhuan.note.dto.PageShareDto;
import com.dinhhuan.note.dto.PmsPageParam;
import com.dinhhuan.note.mapper.PmsBlockMapper;
import com.dinhhuan.note.mapper.PmsPageMapper;
import com.dinhhuan.note.mapper.UmsRoleMapper;
import com.dinhhuan.note.mapper.UmsRoleResourceRelationMapper;
import com.dinhhuan.note.model.*;
import com.dinhhuan.note.service.PmsPageService;
import com.dinhhuan.note.service.UmsUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class PmsPageServiceImpl implements PmsPageService {
    private final Logger logger = LoggerFactory.getLogger(PmsPageServiceImpl.class);
    @Autowired
    private PmsPageMapper pmsPageMapper;
    @Autowired
    private UmsUserService umsUserService;
    @Autowired
    private PmsBlockMapper pmsBlockMapper;
    @Autowired
    private PmsPageDao pmsPageDao;
    @Autowired
    private UmsRoleMapper umsRoleMapper;
    @Autowired
    private UmsRoleResourceRelationMapper umsRoleResourceRelationMapper;
    @Override
    public int create(PmsPageParam param) {
        int count = 0;
        logger.info("Create pmsPage param:{}", param);
        PmsPage pmsPage = new PmsPage();
        BeanUtils.copyProperties(param, pmsPage);
        UmsUser user = umsUserService.getCurrentUser();
        pmsPage.setCreatedBy(user.getId());
        count += pmsPageMapper.insertSelective(pmsPage);
        //create role
        UmsRole role = new UmsRole();
        role.setName("EDITOR");
        role.setStatus(0);
        umsRoleMapper.insertSelective(role);
        //create role relation
        UmsRoleResourceRelation rrrls = new UmsRoleResourceRelation();
        rrrls.setRoleId(role.getId());
        rrrls.setResourceId(pmsPage.getId());
        umsRoleResourceRelationMapper.insertSelective(rrrls);
        return count;
    }

    @Override
    public List<PmsPage> list(Long workspaceId) {
        PmsPageExample example = new PmsPageExample();
        PmsPageExample.Criteria criteria = example.createCriteria();
        criteria.andWorkspaceIdEqualTo(workspaceId);
        return pmsPageMapper.selectByExample(example);
    }

    @Override
    public List<PmsPage> getChildPages (Long pageId) {
        List<PmsPage> pmsPagesChildren;
        PmsPageExample example = new PmsPageExample();
        PmsPageExample.Criteria criteria = example.createCriteria();
        criteria.andParentPageIdEqualTo(pageId);
        pmsPagesChildren = pmsPageMapper.selectByExample(example);
        return pmsPagesChildren;
    }
    @Override
    public int update(Long id, PmsPageParam param) {
         PmsPage pmsPage = new PmsPage();
         BeanUtils.copyProperties(param, pmsPage);
         pmsPage.setId(id);
         return pmsPageMapper.updateByPrimaryKeySelective(pmsPage);
    }

    @Override
    public int deletePage(Long id) {
        PmsBlockExample example = new PmsBlockExample();
        PmsBlockExample.Criteria criteria = example.createCriteria();
        criteria.andPageIdEqualTo(id);
        pmsBlockMapper.deleteByExample(example);
        return pmsPageMapper.deleteByPrimaryKey(id);
    }

    @Override
    public PageShareDto getGeneralInfo(Long pageId) {
        return pmsPageDao.getPage(pageId);
    }

    @Override
    public Integer getPermissionResource(Long id) {
        Integer isEditable = pmsPageDao.getPagePermission(id);
        return isEditable;
    }

    @Override
    public int updatePermissionResource(Long pageId, PagePermissionDto param) {
        int count = pmsPageDao.updatePagePermision(pageId, param.getStatus());
        return count;
    }
}
