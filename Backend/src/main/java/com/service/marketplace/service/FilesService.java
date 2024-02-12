package com.service.marketplace.service;

import com.service.marketplace.dto.request.FilesRequest;
import com.service.marketplace.dto.request.ReviewRequest;
import com.service.marketplace.dto.response.FilesResponse;
import com.service.marketplace.dto.response.ReviewResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface FilesService {
    List<FilesResponse> getAllFiles();
    FilesResponse getFileById(Integer fileId);
    FilesResponse createFile(FilesRequest fileToCreate, MultipartFile multipartFile);
    FilesResponse updateFile(Integer fileId, FilesRequest fileToUpdate, MultipartFile multipartFile);
    void deleteFileById(Integer fileID);
    List<FilesResponse> getFilesByServiceId(Integer serviceId);
    List<FilesResponse> getFilesByReviewId(Integer reviewId);
}
