package com.dinhhuan.note.controller;

import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import com.dinhhuan.note.common.api.CommonResult;
import com.dinhhuan.note.dto.TeamspaceInvitationDto;
import com.dinhhuan.note.dto.WorkspaceMemberParam;
import com.dinhhuan.note.model.NmsWorkspaceMember;
import com.dinhhuan.note.model.UmsInvitation;
import com.dinhhuan.note.service.NmsWorkspaceMemberService;
import com.dinhhuan.note.service.NmsWorkspaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/workspace/member")
public class NmsWorkspaceMemberController {
    @Autowired
    private NmsWorkspaceMemberService nmsWorkspaceMemberService;
    private Log log = LogFactory.get();
    @RequestMapping(value = "", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult<Integer> createMember(@RequestBody WorkspaceMemberParam param) {
        int count = nmsWorkspaceMemberService.createMember(param);
        if(count > 0){
            return CommonResult.success(count);
        }
        return CommonResult.failed();
    }
    @RequestMapping(value="/{id}", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<List<NmsWorkspaceMember>> listMembers(@PathVariable("id") Long workspaceId) {
        List<NmsWorkspaceMember> members = nmsWorkspaceMemberService.listMembers(workspaceId);
        if(members != null && !members.isEmpty()){
            return CommonResult.success(members);
        }
        return CommonResult.failed();
    }
    @RequestMapping(value="/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    public CommonResult<Integer> deleteMember(@PathVariable("id") Long memberId) {
        int count = nmsWorkspaceMemberService.deleteMember(memberId);
        if(count > 0){
            return CommonResult.success(count);
        }
        return CommonResult.failed();
    }
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public CommonResult<Integer> updateMember(@PathVariable("id") Long memberId, WorkspaceMemberParam param) {
        int count = nmsWorkspaceMemberService.updateMember(memberId, param);
        if(count > 0){
            return CommonResult.success(count);
        }
        return CommonResult.failed();
    }
    @RequestMapping(value = "/invitation", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult<Integer> sendInvitation(@RequestBody TeamspaceInvitationDto param) {
        log.info("param {}",param.getObjectId());
        int count = nmsWorkspaceMemberService.saveAndSendInvitation(param);
        if (count > 0){
            return CommonResult.success(count);
        }
        return CommonResult.failed();
    }
    @RequestMapping(value = "/invitations", method=RequestMethod.GET)
    @ResponseBody
    public CommonResult<List<UmsInvitation>> listInvitations() {
        List<UmsInvitation> allInvitations = nmsWorkspaceMemberService.listInvitations();
        if(allInvitations != null && !allInvitations.isEmpty()){
            return CommonResult.success(allInvitations);
        }
        return CommonResult.failed();
    }
    @RequestMapping(value = "/accept/invitation", method=RequestMethod.POST)
    @ResponseBody
    public CommonResult<Integer> acceptInvitation(@RequestBody TeamspaceInvitationDto param) {
        int count = nmsWorkspaceMemberService.acceptInvitation(param.getId());
        if(count > 0){
            return CommonResult.success(count);
        }
        return CommonResult.failed();
    }
    @RequestMapping(value = "/invitation/update-status", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult<Integer> updateStatus(@RequestBody UmsInvitation param) {
        int count = nmsWorkspaceMemberService.updateInvitationStatus(param.getId(), param.getStatus());
        if(count > 0){
            return CommonResult.success(count);
        }
        return CommonResult.failed();
    }
}
