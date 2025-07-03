package com.dinhhuan.note.service.impl;

import com.dinhhuan.note.dao.NmsTeamspaceDao;
import com.dinhhuan.note.dto.TeamspaceParamDto;
import com.dinhhuan.note.dto.WorkspaceMemberParam;
import com.dinhhuan.note.dto.WorkspaceParam;
import com.dinhhuan.note.enums.WorkSpaceRole;
import com.dinhhuan.note.mapper.NmsWorkspaceMapper;
import com.dinhhuan.note.model.NmsWorkspace;
import com.dinhhuan.note.model.UmsUser;
import com.dinhhuan.note.service.NmsTeamspaceService;
import com.dinhhuan.note.service.NmsWorkspaceMemberService;
import com.dinhhuan.note.service.UmsUserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NmsTeamspaceServiceImpl implements NmsTeamspaceService {
    @Autowired
    private UmsUserService umsUserService;
    @Autowired
    private NmsWorkspaceMapper nmsWorkspaceMapper;
    @Autowired
    private NmsWorkspaceMemberService nmsWorkspaceMemberService;
    @Autowired
    private NmsTeamspaceDao teamspaceDao;
    @Override
    public int createTeamspace(WorkspaceParam param) {
        int count = 0;
        NmsWorkspace nmsWorkspace = new NmsWorkspace();
        BeanUtils.copyProperties(param, nmsWorkspace);
        UmsUser creator = umsUserService.getCurrentUser();
        nmsWorkspace.setCreatedBy(creator.getId());
        count += nmsWorkspaceMapper.insert(nmsWorkspace);
        Long workspaceId = nmsWorkspace.getId();
        WorkspaceMemberParam member = new WorkspaceMemberParam();
        member.setWorkspaceId(workspaceId);
        member.setUserId(creator.getId());
        member.setRole(WorkSpaceRole.TEAMSPACE_OWNER.getRole());
        count += nmsWorkspaceMemberService.createMember(member);
        return count;
    }

    @Override
    public List<TeamspaceParamDto> listTeamspace() {
        UmsUser currentUser = umsUserService.getCurrentUser();
        return teamspaceDao.getTeamspaceByUserId(currentUser.getId());
    }

    @Override
    public Integer update(Long wid,WorkspaceParam param) {
        NmsWorkspace nmsWorkspace = new NmsWorkspace();
        nmsWorkspace.setId(wid);
        nmsWorkspace.setName(param.getName());
        nmsWorkspace.setDescription(param.getDescription());
       int count = nmsWorkspaceMapper.updateByPrimaryKeySelective(nmsWorkspace);
       return count;
    }
}
