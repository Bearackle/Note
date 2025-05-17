package com.dinhhuan.note.service;

import com.dinhhuan.note.dto.WorkspaceMemberParam;
import com.dinhhuan.note.model.NmsWorkspaceMember;

import java.util.List;

public interface NmsWorkspaceMemberService {
    int createMember(WorkspaceMemberParam param);
    List<NmsWorkspaceMember> listMembers(Long workspaceId);
    int deleteMember(Long memberId);
    int updateMember(Long memberId,WorkspaceMemberParam param);
}
