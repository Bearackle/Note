package com.dinhhuan.note.service.impl;

import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import com.dinhhuan.note.service.OssService;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.errors.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@Service
public class OssServiceImpl implements OssService {

    @Value("${minio.bucket}")
    private String BUCKET;
    @Autowired
    private MinioClient minioClient;
    private static final Log log = LogFactory.get();
    @Override
    public String uploadFile(MultipartFile file) throws IOException {
        String filename = System.currentTimeMillis() + "-" + file.getOriginalFilename();
        try {
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(BUCKET)
                            .object(filename)
                            .stream(file.getInputStream(), file.getSize(), -1)
                            .contentType(file.getContentType())
                            .build()
            );
        } catch (Exception e) {
            log.error(e);
        }
        return "http://localhost:9002/" + BUCKET + "/" + filename;
    }
}
