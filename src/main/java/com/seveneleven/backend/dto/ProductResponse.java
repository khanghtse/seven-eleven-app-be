package com.seveneleven.backend.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ProductResponse(
        Long id,
        String name,
        String description,
        String category,
        BigDecimal price,
        Integer stock,
        String imageUrl,
        String imagePublicId,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {}
