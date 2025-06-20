package com.dinhhuan.note.controller;

import com.dinhhuan.note.common.api.CommonResult;
import com.dinhhuan.note.dto.PageShareDto;
import com.dinhhuan.note.dto.PmsPageParam;
import com.dinhhuan.note.model.PmsPage;
import com.dinhhuan.note.model.PmsPageExample;
import com.dinhhuan.note.service.PmsPageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/page")
public class PmsPageController {
    @Autowired
    private PmsPageService pmsPageService;

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult<Integer> create(@RequestBody PmsPageParam param){
        int count = pmsPageService.create(param);
        if(count > 0){
            return CommonResult.success(count);
        }
        return CommonResult.failed();
    }
    @RequestMapping(value = "/list/{id}", method= RequestMethod.GET)
    @ResponseBody
    public CommonResult<List<PmsPage>> list(@PathVariable("id") Long workspaceId){
        List<PmsPage> list = pmsPageService.list(workspaceId);
        if(list != null && !list.isEmpty()){
            return CommonResult.success(list);
        }
        return CommonResult.failed();
    }
    @RequestMapping(value = "/list/{id}/child", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<List<PmsPage>> listChildren(@PathVariable("id") Long workspaceId) {
        List<PmsPage> list = pmsPageService.getChildPages(workspaceId);
        if (list != null && !list.isEmpty()) {
            return CommonResult.success(list);
        }
        return CommonResult.failed();
    }
    @RequestMapping(value = "/{id}" , method = RequestMethod.PUT)
    @ResponseBody
    public CommonResult<Integer> update(@PathVariable("id") Long pageId, @RequestBody PmsPageParam param){
        int count = pmsPageService.update(pageId, param);
        if(count > 0){
            return CommonResult.success(count);
        }
        return CommonResult.failed();
    }
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public CommonResult<Integer> deletePage(@PathVariable("id") Long pageId){
        int count = pmsPageService.deletePage(pageId);
        if(count > 0){
            return CommonResult.success(count);
        }
        return CommonResult.failed();
    }
    @RequestMapping(value = "/{id}/share-info", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<PageShareDto> getPageShareInfo(@PathVariable("id") Long pageId){
        PageShareDto page = this.pmsPageService.getGeneralInfo(pageId);
        if(page != null){
            return CommonResult.success(page);
        }
        return CommonResult.failed();
    }
}
