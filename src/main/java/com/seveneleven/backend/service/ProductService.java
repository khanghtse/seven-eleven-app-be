package com.seveneleven.backend.service;

import com.seveneleven.backend.dto.ProductResponse;
import com.seveneleven.backend.entity.Product;
import com.seveneleven.backend.exception.ResourceNotFoundException;
import com.seveneleven.backend.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService implements IProductService {

    private final ProductRepository productRepository;
    private final CloudinaryService cloudinaryService;

    @Override
    public List<ProductResponse> findAll() {
        return productRepository.findAll().stream().map(this::toResponse).toList();
    }

    @Override
    public ProductResponse findById(Long id) {
        return toResponse(getProduct(id));
    }

    @Transactional
    @Override
    public ProductResponse create(String name, String description, String category, BigDecimal price, Integer stock, MultipartFile image) {
        Product product = new Product();
        product.setName(name);
        product.setDescription(description);
        product.setCategory(category);
        product.setPrice(price);
        product.setStock(stock);

        if (image != null && !image.isEmpty()) {
            CloudinaryService.UploadResult upload = cloudinaryService.upload(image);
            product.setImageUrl(upload.url());
            product.setImagePublicId(upload.publicId());
        }

        return toResponse(productRepository.save(product));
    }

    @Override
    @Transactional
    public ProductResponse update(Long id, String name, String description, String category, BigDecimal price, Integer stock, MultipartFile image) {
        Product product = getProduct(id);
        product.setName(name);
        product.setDescription(description);
        product.setCategory(category);
        product.setPrice(price);
        product.setStock(stock);

        if (image != null && !image.isEmpty()) {
            cloudinaryService.delete(product.getImagePublicId());
            CloudinaryService.UploadResult upload = cloudinaryService.upload(image);
            product.setImageUrl(upload.url());
            product.setImagePublicId(upload.publicId());
        }

        return toResponse(productRepository.save(product));
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Product product = getProduct(id);
        cloudinaryService.delete(product.getImagePublicId());
        productRepository.delete(product);
    }

    @Override
    public List<ProductResponse> searchProducts(String keyword) {
        return productRepository.findByNameContainingIgnoreCase(keyword).stream().map(this::toResponse).toList();
    }

    public Product getProduct(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id " + id));
    }

    private ProductResponse toResponse(Product p) {
        return new ProductResponse(
                p.getId(),
                p.getName(),
                p.getDescription(),
                p.getCategory(),
                p.getPrice(),
                p.getStock(),
                p.getImageUrl(),
                p.getImagePublicId(),
                p.getCreatedAt(),
                p.getUpdatedAt()
        );
    }
}
