package com.dinhhuan.note.service;

import com.dinhhuan.note.dto.TeamspaceInvitationDto;
import com.dinhhuan.note.dto.WorkspaceMemberParam;
import com.dinhhuan.note.model.NmsWorkspaceMember;
import com.dinhhuan.note.model.UmsInvitation;

import java.util.List;

public interface NmsWorkspaceMemberService {
    int saveAndSendInvitation(TeamspaceInvitationDto invitation);
    int createMember(WorkspaceMemberParam param);
    List<NmsWorkspaceMember> listMembers(Long workspaceId);
    int deleteMember(Long memberId);
    int updateMember(Long memberId,WorkspaceMemberParam param);
    List<UmsInvitation> listInvitations();
    int updateInvitationStatus(Long id, String status);
    int acceptInvitation(Long id);
}
