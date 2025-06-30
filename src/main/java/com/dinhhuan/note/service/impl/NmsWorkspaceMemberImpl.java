package com.dinhhuan.note.service.impl;

import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import com.dinhhuan.note.dto.InvitationMessage;
import com.dinhhuan.note.dto.TeamspaceInvitationDto;
import com.dinhhuan.note.dto.WorkspaceMemberParam;
import com.dinhhuan.note.mapper.NmsWorkspaceMapper;
import com.dinhhuan.note.mapper.NmsWorkspaceMemberMapper;
import com.dinhhuan.note.mapper.UmsInvitationMapper;
import com.dinhhuan.note.model.*;
import com.dinhhuan.note.service.NmsWorkspaceMemberService;
import com.dinhhuan.note.service.NmsWorkspaceService;
import com.dinhhuan.note.service.NotificationService;
import com.dinhhuan.note.service.UmsUserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NmsWorkspaceMemberImpl implements NmsWorkspaceMemberService {
    @Autowired
    private NmsWorkspaceMemberMapper nmsWorkspaceMemberMapper;
    @Autowired
    private UmsInvitationMapper umsInvitationMapper;
    @Autowired
    private UmsUserService umsUserService;
    @Autowired
    private NotificationService notificationService;
    private Log log = LogFactory.get();
    @Autowired
    private NmsWorkspaceMapper nmsWorkspaceMapper;

    @Override
    public int createMember(WorkspaceMemberParam param) {
        int count = 0;
        NmsWorkspaceMember member = new NmsWorkspaceMember();
        BeanUtils.copyProperties(param, member);
        count = nmsWorkspaceMemberMapper.insert(member);
        return count;
    }
    @Override
    public List<NmsWorkspaceMember> listMembers(Long workspaceId) {
        NmsWorkspaceMemberExample example = new NmsWorkspaceMemberExample();
        NmsWorkspaceMemberExample.Criteria criteria = example.createCriteria();
        criteria.andWorkspaceIdEqualTo(workspaceId);
        return nmsWorkspaceMemberMapper.selectByExample(example);
    }

    @Override
    public int deleteMember(Long memberId) {
        int count = 0;
        count = nmsWorkspaceMemberMapper.deleteByPrimaryKey(memberId);
        return count;
    }

    @Override
    public int updateMember(Long memberId, WorkspaceMemberParam param) {
        NmsWorkspaceMember member = new NmsWorkspaceMember();
        BeanUtils.copyProperties(param, member);
        member.setUserId(memberId);
        NmsWorkspaceMemberExample example = new NmsWorkspaceMemberExample();
        NmsWorkspaceMemberExample.Criteria criteria = example.createCriteria();
        criteria.andUserIdEqualTo(memberId);
        return nmsWorkspaceMemberMapper.updateByExample(member, example);
    }
    @Override
    public int saveAndSendInvitation(TeamspaceInvitationDto invitation) {
        UmsInvitation invitationEntity = new UmsInvitation();
        log.info("invitationEntity {}", invitationEntity);
        invitationEntity.setInviteeEmail(invitation.getInviteeEmail());
        invitationEntity.setObjectId(invitation.getObjectId());
        invitationEntity.setStatus("pending");
        String workspaceName = getWorkspaceName(invitation.getObjectId());
        invitationEntity.setObjectType(workspaceName);
        invitationEntity.setRole(invitation.getRole());
        invitationEntity.setInviterId(umsUserService.getCurrentUser().getId());
        invitationEntity.setId(null);
        int count = umsInvitationMapper.insertSelective(invitationEntity);
        sendInvitation(invitationEntity.getId(), invitation);
        return count;
    }
    @Override
    public List<UmsInvitation> listInvitations() {
        UmsInvitationExample example = new UmsInvitationExample();
        UmsInvitationExample.Criteria criteria = example.createCriteria();
        criteria.andInviteeEmailEqualTo(umsUserService.getCurrentUser().getEmail());
        List<UmsInvitation> invitations = umsInvitationMapper.selectByExample(example);
        return invitations;
    }
    public void sendInvitation(Long invitationId,TeamspaceInvitationDto invitationDto){
        InvitationMessage invitationMessage = new InvitationMessage();
        UmsUser user = umsUserService.getCurrentUser();
        UmsUser inviteeUser = umsUserService.getUserByEmail(invitationDto.getInviteeEmail());
        if(inviteeUser == null) {
            return;
        }
        String workspaceName = getWorkspaceName(invitationDto.getObjectId());
        invitationMessage.setInviterEmail(user.getEmail());
        invitationMessage.setInviteeEmail(inviteeUser.getEmail());
        invitationMessage.setTeamspaceName(workspaceName);
        invitationMessage.setInviteeUsername(inviteeUser.getUsername());
        invitationMessage.setId(invitationId);
        invitationMessage.setStatus("pending");
        log.info("sendInvitation {}", invitationMessage.toString());
        notificationService.sendNotification(invitationMessage);
    }
    public String getWorkspaceName(long workspaceId) {
        NmsWorkspaceExample example = new NmsWorkspaceExample();
        NmsWorkspaceExample.Criteria criteria = example.createCriteria();
        criteria.andIdEqualTo(workspaceId);
        List<NmsWorkspace> list = nmsWorkspaceMapper.selectByExample(example);
        if(list != null && !list.isEmpty()) {
            log.info("FOUND WORKSPACE {}", list.get(0).getName());
            return list.get(0).getName();
        }
        log.info("NO WORKSPACE NAME");
        return "NOT FOUND";
    }
}
