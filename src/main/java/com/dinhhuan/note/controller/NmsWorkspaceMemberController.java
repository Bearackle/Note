package com.dinhhuan.note.controller;

import com.dinhhuan.note.common.api.CommonResult;
import com.dinhhuan.note.dto.WorkspaceMemberParam;
import com.dinhhuan.note.model.NmsWorkspaceMember;
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
}
