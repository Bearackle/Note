package com.dinhhuan.note.service.impl;

import com.dinhhuan.note.dto.WorkspaceMemberParam;
import com.dinhhuan.note.mapper.NmsWorkspaceMemberMapper;
import com.dinhhuan.note.model.NmsWorkspaceMember;
import com.dinhhuan.note.model.NmsWorkspaceMemberExample;
import com.dinhhuan.note.service.NmsWorkspaceMemberService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NmsWorkspaceMemberImpl implements NmsWorkspaceMemberService {
    @Autowired
    private NmsWorkspaceMemberMapper nmsWorkspaceMemberMapper;
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
}
