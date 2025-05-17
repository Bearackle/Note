package com.dinhhuan.note.controller;

import com.dinhhuan.note.common.api.CommonResult;
import com.dinhhuan.note.dto.PmsBlockParam;
import com.dinhhuan.note.model.PmsBlock;
import com.dinhhuan.note.service.PmsBlockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/block")
public class PmsBlockController {
    @Autowired
    private PmsBlockService pmsBlockService;
    @RequestMapping(value = "", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult<Integer> createBlock(@RequestBody PmsBlockParam param){
        int count = pmsBlockService.create(param);
        if(count > 0){
            return CommonResult.success(count);
        }
        return CommonResult.failed();
    }
    @RequestMapping(value="/{id}", method = RequestMethod.PUT)
    public CommonResult<Integer> updateBlock(@PathVariable("id") Long blockId, @RequestBody PmsBlockParam param){
        int count = pmsBlockService.update(blockId, param);
        if(count > 0){
            return CommonResult.success(count);
        }
        return CommonResult.failed();
    }
    @RequestMapping(value ="/list/{id}",method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<List<PmsBlock>> list(@PathVariable("id") Long pageId){
        List<PmsBlock> list = pmsBlockService.list(pageId);
        if(list != null && list.size() > 0){
            return CommonResult.success(list);
        }
        return CommonResult.failed();
    }
    @RequestMapping(value = "/order/{id}", method = RequestMethod.PUT)
    @ResponseBody
    public CommonResult<Integer> updateOrder(@PathVariable("id") Long blockId, @RequestBody PmsBlockParam param){
        int count = pmsBlockService.update(blockId, param);
        if(count > 0){
            return CommonResult.success(count);
        }
        return CommonResult.failed();
    }
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    public CommonResult<Integer> delete(@PathVariable("id") Long id){
        int count = pmsBlockService.delete(id);
        if(count > 0){
            return CommonResult.success(count);
        }
        return CommonResult.failed();
    }
}
