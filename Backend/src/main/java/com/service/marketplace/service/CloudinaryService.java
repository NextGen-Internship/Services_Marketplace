package com.service.marketplace.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;

public interface CloudinaryService {
    String uploadFile(MultipartFile multipartFile) throws IOException;
    String deleteFile();
    String getPicture() throws MalformedURLException;
}
