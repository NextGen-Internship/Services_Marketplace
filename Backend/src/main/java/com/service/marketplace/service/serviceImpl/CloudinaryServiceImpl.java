package com.service.marketplace.service.serviceImpl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.service.marketplace.persistence.entity.Files;
import com.service.marketplace.persistence.entity.Review;
import com.service.marketplace.persistence.entity.User;
import com.service.marketplace.persistence.repository.ReviewRepository;
import com.service.marketplace.persistence.repository.ServiceRepository;
import com.service.marketplace.persistence.repository.UserRepository;
import com.service.marketplace.service.CloudinaryService;
import com.service.marketplace.service.ServiceService;
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

    @Value("${Cloudinary_cloud_name}")
    private String folder;

    @Override
    public String uploadFile(MultipartFile file) throws IOException {
        Map<?, ?> options = Map.of(
                "folder", folder
        );

        Map<?, ?> uploadResult = cloudinary.uploader().upload(file.getBytes(), options);
        String pictureUrl = (String) uploadResult.get("secure_url");

        return pictureUrl;
    }

    @Override
    public String deleteFile(String pictureUrl) {
        if (pictureUrl == null) {
            return "There is no such a picture.";
        }

        String publicId = extractPublicIdFromUrl(pictureUrl);
        try {
            cloudinary.uploader().destroy(publicId, null);
            return "File has been deleted.";
        } catch (IOException e) {
            return "Error deleting file: " + e.getMessage();
        }
    }

    @Override
    public List<String> getAllPictures() {
        List<String> pictureUrls = new ArrayList<>();

        try {
            Map response = cloudinary.api().resources(ObjectUtils.asMap("type", "upload", "max_results", 500));

            // Extract URLs from the response
            List<Map<String, Object>> resources = (List<Map<String, Object>>) response.get("resources");
            for (Map<String, Object> resource : resources) {
                String url = (String) resource.get("secure_url");
                pictureUrls.add(url);
            }
        } catch (Exception e) {
            // Handle exceptions, such as Cloudinary API errors
            e.printStackTrace();
            // You might want to log or handle the exception in a different way
        }

        return pictureUrls;
    }

    public String generateFilename(MultipartFile file) {
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
