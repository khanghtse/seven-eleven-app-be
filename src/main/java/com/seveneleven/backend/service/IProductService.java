package com.seveneleven.backend.service;

import com.seveneleven.backend.dto.ProductResponse;
import com.seveneleven.backend.entity.Product;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.List;

public interface IProductService {
    List<ProductResponse> findAll();
    ProductResponse findById(Long id);
    ProductResponse create(String name, String description, String category, BigDecimal price, Integer stock, MultipartFile image);
    ProductResponse update(Long id, String name, String description, String category, BigDecimal price, Integer stock, MultipartFile image);
    void delete(Long id);
    List<ProductResponse> searchProducts(String keyword);
}
