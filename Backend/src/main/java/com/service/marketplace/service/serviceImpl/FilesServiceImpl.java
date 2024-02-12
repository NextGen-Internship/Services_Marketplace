package com.service.marketplace.service.serviceImpl;

import com.service.marketplace.dto.request.FilesRequest;
import com.service.marketplace.dto.response.FilesResponse;
import com.service.marketplace.mapper.FilesMapper;
import com.service.marketplace.persistence.entity.Files;
import com.service.marketplace.persistence.entity.Review;
import com.service.marketplace.persistence.entity.User;
import com.service.marketplace.persistence.repository.FilesRepository;
import com.service.marketplace.persistence.repository.ReviewRepository;
import com.service.marketplace.persistence.repository.ServiceRepository;
import com.service.marketplace.service.CloudinaryService;
import com.service.marketplace.service.FilesService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FilesServiceImpl implements FilesService {
    private final FilesRepository filesRepository;
    private final FilesMapper filesMapper;
    private final ReviewRepository reviewRepository;
    private final ServiceRepository serviceRepository;
    private final CloudinaryService cloudinaryService;

    @Override
    public List<FilesResponse> getAllFiles() {
        List<Files> files = filesRepository.findAll();

        return filesMapper.toFilesResponseList(files);
    }

    @Override
    public FilesResponse getFileById(Integer fileId) {
        Files file = filesRepository.findById(fileId).orElse(null);

        if (file != null) {
            return filesMapper.filesToFilesResponse(file);
        } else {
            return null;
        }
    }

    @Override
    public FilesResponse createFile(FilesRequest fileToCreate, MultipartFile multipartFile) {
        Review review = null;
        com.service.marketplace.persistence.entity.Service service = null;

        if (fileToCreate.getReviewId() != null) {
            review = reviewRepository.findById(fileToCreate.getReviewId()).orElse(null);
        } else if (fileToCreate.getServiceId() != null) {
            service = serviceRepository.findById(fileToCreate.getServiceId()).orElse(null);
        }

        Files newFile = null;

        if (review != null) {
            newFile = filesMapper.filesRequestToFiles(fileToCreate, review);
        } else if (service != null) {
            newFile = filesMapper.filesRequestToFiles(fileToCreate, service);
        }

        if (newFile == null) {
            throw new RuntimeException();
        } else {
            String newPictureUrl;

            try {
                newPictureUrl = cloudinaryService.uploadFile(multipartFile);
            } catch (IOException e) {
                String errorMessage = "Error uploading picture";
                throw new RuntimeException(errorMessage, e);
            }

            newFile.setUrl(newPictureUrl);
            return filesMapper.filesToFilesResponse(filesRepository.save(newFile));
        }
    }

    @Override
    public FilesResponse updateFile(Integer fileId, FilesRequest fileToUpdate, MultipartFile multipartFile) {
        Files existingFile = filesRepository.findById(fileId).orElse(null);

        Review review = null;
        com.service.marketplace.persistence.entity.Service service = null;

        if (fileToUpdate.getReviewId() != null) {
            review = reviewRepository.findById(fileToUpdate.getReviewId()).orElse(null);
        } else if (fileToUpdate.getServiceId() != null) {
            service = serviceRepository.findById(fileToUpdate.getServiceId()).orElse(null);
        }

        Files updatedFile = null;

        if (review != null) {
            updatedFile = filesMapper.filesRequestToFiles(fileToUpdate, review);
        } else if (service != null) {
            updatedFile = filesMapper.filesRequestToFiles(fileToUpdate, service);
        }

        if (multipartFile != null) {
            String newPictureUrl;

            try {
                newPictureUrl = cloudinaryService.uploadFile(multipartFile);
            } catch (IOException e) {
                String errorMessage = "Error uploading picture";
                throw new RuntimeException(errorMessage, e);
            }

            if (updatedFile != null) {
                updatedFile.setUrl(newPictureUrl);
            } else {
                throw new RuntimeException("The updated file is null.");
            }
        }

        if (existingFile != null) {
            if (multipartFile != null) {
                existingFile.setUrl(updatedFile.getUrl());
            }

            return filesMapper.filesToFilesResponse(filesRepository.save(existingFile));
        } else {
            return null;
        }
    }

    @Override
    public void deleteFileById(Integer fileId) {
        filesRepository.deleteById(fileId);
    }

    @Override
    public List<FilesResponse> getFilesByServiceId(Integer serviceId) {
        com.service.marketplace.persistence.entity.Service service = serviceRepository.findById(serviceId).orElse(null);

        if (service != null) {
            List<Files> serviceFiles = filesRepository.findByService(service);

            return filesMapper.toFilesResponseList(serviceFiles);
        } else {
            return Collections.emptyList();
        }
    }

    @Override
    public List<FilesResponse> getFilesByReviewId(Integer reviewId) {
        Review review = reviewRepository.findById(reviewId).orElse(null);

        if (review != null) {
            List<Files> reviewFiles = filesRepository.findByReview(review);

            return filesMapper.toFilesResponseList(reviewFiles);
        } else {
            return Collections.emptyList();
        }
    }
}
