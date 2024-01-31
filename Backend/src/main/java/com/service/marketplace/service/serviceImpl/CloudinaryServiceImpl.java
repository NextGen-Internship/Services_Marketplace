package com.service.marketplace.service.serviceImpl;

import com.cloudinary.Cloudinary;
import com.service.marketplace.persistence.entity.User;
import com.service.marketplace.persistence.repository.UserRepository;
import com.service.marketplace.service.CloudinaryService;
import com.service.marketplace.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CloudinaryServiceImpl implements CloudinaryService {
    private final Cloudinary cloudinary;
    private final UserService userService;
    private final UserRepository userRepository;

    @Value("${Cloudinary_cloud_name}")
    private String folder;

    @Override
    public String uploadFile(MultipartFile file) throws IOException {
        String filename = generateFilename(file);
        Map<?, ?> options = Map.of(
                "folder", folder
        );

        Map<?, ?> uploadResult = cloudinary.uploader().upload(file.getBytes(), options);
        String pictureUrl = (String) uploadResult.get("secure_url");

        User user = userService.getCurrentUser();
        user.setPicture(String.valueOf(pictureUrl));
        userRepository.save(user);

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
