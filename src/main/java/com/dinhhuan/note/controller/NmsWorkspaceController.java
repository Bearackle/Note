package com.dinhhuan.note.controller;

import com.dinhhuan.note.common.api.CommonResult;
import com.dinhhuan.note.dto.TeamspaceParamDto;
import com.dinhhuan.note.dto.WorkspaceParam;
import com.dinhhuan.note.model.NmsWorkspace;
import com.dinhhuan.note.service.NmsTeamspaceService;
import com.dinhhuan.note.service.NmsWorkspaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.nio.file.Path;
import java.util.List;

@RestController
@RequestMapping("/workspace")
public class NmsWorkspaceController {
    @Autowired
    private NmsWorkspaceService nmsWorkspaceService;
    @Autowired
    private NmsTeamspaceService nmsTeamspaceService;

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult<Integer> create(@RequestBody WorkspaceParam param) {
        int count = nmsWorkspaceService.createWorkspace(param);
        if(count > 0 ){
            return CommonResult.success(count);
        }
        return CommonResult.failed();
    }
    @RequestMapping(value = "/info/{id}", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<NmsWorkspace> getInfo(@PathVariable Long id){
        NmsWorkspace workspace = nmsWorkspaceService.getWorkspaceInfo(id);
        if(workspace != null){
            return CommonResult.success(workspace);
        }
        return CommonResult.failed();
    }
    @RequestMapping(value="/list")
    @ResponseBody
    public CommonResult<List<NmsWorkspace>> list(){
        List<NmsWorkspace> list = nmsWorkspaceService.listWorkspaces();
        if(list != null && !list.isEmpty()) {
            return CommonResult.success(list);
        }
        return CommonResult.failed();
    }
    @RequestMapping(value = "/update/{id}", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult<Integer> updateWorkspace(@PathVariable("id") Long wid, @RequestBody WorkspaceParam param) {
        Integer count = nmsTeamspaceService.update(wid, param);
        if(count != null && count > 0) {
            return CommonResult.success(count);
        } else
            return CommonResult.failed();
    }
    @RequestMapping(value = "/create-team", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult<Integer> createTeamspace(@RequestBody WorkspaceParam param){
       int count = this.nmsTeamspaceService.createTeamspace(param);
       if(count > 0 ){
           return CommonResult.success(count);
       }
       return CommonResult.failed();
    }
    @RequestMapping(value = "/teamspace", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<List<TeamspaceParamDto>> getTeamspace(){
        List<TeamspaceParamDto> teamspaces = nmsTeamspaceService.listTeamspace();
        if(teamspaces != null && !teamspaces.isEmpty()) {
            return CommonResult.success(teamspaces);
        } else
            return CommonResult.failed();
    }
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    public CommonResult<Integer> delete(@PathVariable("id") Long wid) {
        int count = nmsWorkspaceService.delete(wid);
        if(count > 0 ){
            return CommonResult.success(count);
        }
        return CommonResult.failed();
    }
}
