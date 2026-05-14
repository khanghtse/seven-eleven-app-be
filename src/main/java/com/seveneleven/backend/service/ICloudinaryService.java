package com.seveneleven.backend.service;

import com.seveneleven.backend.service.impl.CloudinaryService;
import org.springframework.web.multipart.MultipartFile;

public interface ICloudinaryService {

    CloudinaryService.UploadResult upload(MultipartFile file);
    void delete(String publicId);
}
