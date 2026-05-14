package com.seveneleven.backend.controller;

import com.seveneleven.backend.dto.ProductResponse;
import com.seveneleven.backend.service.IProductService;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final IProductService productService;

    @GetMapping
    public List<ProductResponse> getAll() {
        return productService.findAll();
    }

    @GetMapping("/{id}")
    public ProductResponse getById(@PathVariable Long id) {
        return productService.findById(id);
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ProductResponse create(
            @RequestParam @NotBlank String name,
            @RequestParam(required = false) String description,
            @RequestParam(required = false) String category,
            @RequestParam @NotNull @DecimalMin("0.0") BigDecimal price,
            @RequestParam @NotNull Integer stock,
            @RequestPart(required = false) MultipartFile image
    ) {
        return productService.create(name, description, category, price, stock, image);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ProductResponse update(
            @PathVariable Long id,
            @RequestParam @NotBlank String name,
            @RequestParam(required = false) String description,
            @RequestParam(required = false) String category,
            @RequestParam @NotNull @DecimalMin("0.0") BigDecimal price,
            @RequestParam @NotNull Integer stock,
            @RequestPart(required = false) MultipartFile image
    ) {
        return productService.update(id, name, description, category, price, stock, image);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        productService.delete(id);
    }

    @GetMapping("/search")
    public List<ProductResponse> searchProducts(
            @RequestParam String keyword
    ) {
        return productService.searchProducts(keyword);
    }
}
