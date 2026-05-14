package com.seveneleven.backend.dto;

import com.seveneleven.backend.entity.OrderStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record OrderResponse(
        Long id,
        String customerName,
        String phone,
        String address,
        BigDecimal totalAmount,
        OrderStatus status,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        List<OrderItemResponse> items
) {}
