package com.dinhhuan.note.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface OssService {
    String uploadFile(MultipartFile file) throws IOException;
}
