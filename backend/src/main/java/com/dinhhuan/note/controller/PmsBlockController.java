package com.dinhhuan.note.controller;

import com.dinhhuan.note.common.api.CommonResult;
import com.dinhhuan.note.dto.PmsBlockParam;
import com.dinhhuan.note.dto.SearchBlockObject;
import com.dinhhuan.note.model.PmsBlock;
import com.dinhhuan.note.service.PmsBlockService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/block")
public class PmsBlockController {
    @Autowired
    private PmsBlockService pmsBlockService;

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult<Integer> createBlock(@RequestBody PmsBlockParam param){
        int count = pmsBlockService.create(param);
        if(count > 0){
            return CommonResult.success(count);
        }
        return CommonResult.failed();
    }
    @RequestMapping(value="/{idBlock}", method = RequestMethod.PUT)
    public CommonResult<Integer> updateBlock( @PathVariable("idBlock") String idBlock, @RequestBody PmsBlockParam param){
        log.info("param: {}", param);
        int count = pmsBlockService.update(idBlock,param);
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
        int count = pmsBlockService.updateOrder(blockId, param);
        if(count > 0){
            return CommonResult.success(count);
        }
        return CommonResult.failed();
    }
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    public CommonResult<Integer> delete(@PathVariable("id") String idBlock){
        int count = pmsBlockService.delete(idBlock);
        if(count > 0){
            return CommonResult.success(count);
        }
        return CommonResult.failed();
    }
    @RequestMapping(value = "/search", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<List<SearchBlockObject>> searchBlockContent(@RequestParam("keyword") String keyword){
        List<SearchBlockObject> objects = pmsBlockService.search(keyword);
        if(objects != null && !objects.isEmpty()){
            return CommonResult.success(objects);
        }
        return CommonResult.failed();
    }
}
