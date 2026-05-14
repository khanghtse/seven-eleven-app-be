package com.seveneleven.backend.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CloudinaryService implements ICloudinaryService {

    private final Cloudinary cloudinary;

    @Override
    public UploadResult upload(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            return null;
        }
        try {
            Map<?, ?> result = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.emptyMap());
            String url = (String) result.get("secure_url");
            String publicId = (String) result.get("public_id");
            return new UploadResult(url, publicId);
        } catch (IOException e) {
            throw new RuntimeException("Upload to Cloudinary failed", e);
        }
    }

    @Override
    public void delete(String publicId) {
        if (publicId == null || publicId.isBlank()) return;
        try {
            cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
        } catch (IOException e) {
            throw new RuntimeException("Delete from Cloudinary failed", e);
        }
    }

    public record UploadResult(String url, String publicId) {}
}
