package com.service.marketplace.service.serviceImpl;

import com.cloudinary.Cloudinary;
import com.service.marketplace.persistence.entity.Files;
import com.service.marketplace.persistence.entity.Review;
import com.service.marketplace.persistence.entity.User;
import com.service.marketplace.persistence.repository.ReviewRepository;
import com.service.marketplace.persistence.repository.ServiceRepository;
import com.service.marketplace.persistence.repository.UserRepository;
import com.service.marketplace.service.CloudinaryService;
import com.service.marketplace.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CloudinaryServiceImpl implements CloudinaryService {
    private final Cloudinary cloudinary;
    private final UserService userService;
    private final UserRepository userRepository;
    private final ServiceRepository serviceRepository;
    private final ReviewRepository reviewRepository;

    @Value("${Cloudinary_cloud_name}")
    private String folder;

    @Override
    public String uploadFile(MultipartFile file, String entityType, Integer entityId) throws IOException {
        String filename = generateFilename(file);
        Map<?, ?> options = Map.of(
                "folder", folder
        );

        Map<?, ?> uploadResult = cloudinary.uploader().upload(file.getBytes(), options);
        String pictureUrl = (String) uploadResult.get("secure_url");

        switch (entityType) {
            case "USER":
                User user = userService.getCurrentUser();

                if (user == null) {
                    throw new EntityNotFoundException("User not found");
                }

                user.setPicture(String.valueOf(pictureUrl));
                userRepository.save(user);
                break;
            case "SERVICE":
                com.service.marketplace.persistence.entity.Service service = serviceRepository.findById(entityId).orElseThrow(() -> new EntityNotFoundException("Service not found"));
                service.setPicture(String.valueOf(pictureUrl));
                serviceRepository.save(service);
                break;
            case "REVIEW":
                Review review = reviewRepository.findById(entityId).orElseThrow(() -> new EntityNotFoundException("Review not found"));
                List<Files> reviewFiles = review.getFilesList();
                Files files = new Files(String.valueOf(pictureUrl), LocalDateTime.now().plusMonths(3));
                reviewFiles.add(files);
                review.setFilesList(reviewFiles);
                reviewRepository.save(review);
                break;
            default:
                throw new IllegalArgumentException("Invalid entity type");
        }

        return pictureUrl;
    }

    @Override
    public String deleteFile() {
        User user = userService.getCurrentUser();
        String pictureUrl = user.getPicture();
        if (pictureUrl == null) {
            return "User with ID " + user.getId() + " does not have a picture to delete.";
        }

        String publicId = extractPublicIdFromUrl(pictureUrl);
        try {
            cloudinary.uploader().destroy(publicId, null);
            user.setPicture(null);
            return "File for user with ID " + user.getId() + " has been deleted.";
        } catch (IOException e) {
            return "Error deleting file for user with ID " + user.getId() + ": " + e.getMessage();
        }
    }

    @Override
    public String getPicture() {
        int userId = userService.getCurrentUser().getId();
        User user = userRepository.findById(userId).orElse(null);
        return user != null ? user.getPicture() : null;
    }

    private String generateFilename(MultipartFile file) {
        return folder + "/" + UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
    }

    private String extractPublicIdFromUrl(String url) {
        // Assuming the public ID is the part between the last '/' and the '.' in the URL
        int lastSlashIndex = url.lastIndexOf('/');
        int dotIndex = url.lastIndexOf('.');
        if (lastSlashIndex != -1 && dotIndex != -1 && dotIndex > lastSlashIndex) {
            return url.substring(lastSlashIndex + 1, dotIndex);
        } else {
            throw new IllegalArgumentException("Invalid URL format: " + url);
        }
    }

}
