package com.dinhhuan.note.service.impl;

import com.dinhhuan.note.dto.WorkspaceMemberParam;
import com.dinhhuan.note.dto.WorkspaceParam;
import com.dinhhuan.note.mapper.NmsWorkspaceMapper;
import com.dinhhuan.note.model.NmsWorkspace;
import com.dinhhuan.note.model.NmsWorkspaceExample;
import com.dinhhuan.note.model.NmsWorkspaceMember;
import com.dinhhuan.note.model.UmsUser;
import com.dinhhuan.note.service.NmsWorkspaceMemberService;
import com.dinhhuan.note.service.NmsWorkspaceService;
import com.dinhhuan.note.service.UmsUserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NmsWorkspaceServiceImpl implements NmsWorkspaceService {
    @Autowired
    private UmsUserService umsUserService;
    @Autowired
    private NmsWorkspaceMemberService nmsWorkspaceMemberService;
    @Autowired
    private NmsWorkspaceMapper nmsWorkspaceMapper;
    @Override
    public int createWorkspace(WorkspaceParam param) {
        int count = 0;
        NmsWorkspace nmsWorkspace = new NmsWorkspace();
        BeanUtils.copyProperties(param, nmsWorkspace);
        UmsUser creator = umsUserService.getCurrentUser();
        nmsWorkspace.setCreatedBy(creator.getId());
        count += nmsWorkspaceMapper.insert(nmsWorkspace);
        //create owner
        Long workspaceId = nmsWorkspace.getId();
        WorkspaceMemberParam member = new WorkspaceMemberParam();
        member.setWorkspaceId(workspaceId);
        member.setUserId(creator.getId());
        member.setRole("owner");
        count += nmsWorkspaceMemberService.createMember(member);
        return count;
    }
    @Override
    public NmsWorkspace getWorkspaceInfo(Long id) {
        NmsWorkspaceExample example = new NmsWorkspaceExample();
        NmsWorkspaceExample.Criteria criteria = example.createCriteria();
        criteria.andIdEqualTo(id);
        List<NmsWorkspace> list = nmsWorkspaceMapper.selectByExample(example);
        if(list != null && !list.isEmpty()) {
            return list.get(0);
        }
        return null;
    }

    @Override
    public List<NmsWorkspace> listWorkspaces() {
        UmsUser user = umsUserService.getCurrentUser();
        NmsWorkspaceExample example = new NmsWorkspaceExample();
        NmsWorkspaceExample.Criteria criteria = example.createCriteria();
        criteria.andCreatedByEqualTo(user.getId());
        List<NmsWorkspace> list = nmsWorkspaceMapper.selectByExampleWithBLOBs(example);
        if(list != null && !list.isEmpty()) {
            return list;
        }
        return null;
    }
}
