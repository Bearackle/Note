package com.dinhhuan.note.controller;

import cn.hutool.json.JSONObject;
import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import com.dinhhuan.note.service.OssService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/oss")
public class OssController {
    private static final Log log = LogFactory.get();
    @Autowired
    private OssService ossService;
    @RequestMapping(value = "/upload",method = RequestMethod.POST)
    @ResponseBody
    public JSONObject upload(@RequestParam("image") MultipartFile file) {
        log.info("file upload", file.getOriginalFilename());
        JSONObject result = new JSONObject();
        try{
            String url = ossService.uploadFile(file);
            result.set("success", 1);
            JSONObject f = new JSONObject();
            f.set("url", url);
            result.set("file", f);
        } catch (IOException e){
            log.error(e.getMessage());
            result.set("success", 0);
        }
        return result;
    }
}
