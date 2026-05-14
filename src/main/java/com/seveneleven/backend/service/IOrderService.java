package com.seveneleven.backend.service;

import com.seveneleven.backend.dto.CreateOrderRequest;
import com.seveneleven.backend.dto.OrderResponse;

import java.util.List;

public interface IOrderService {
    List<OrderResponse> findAll();
    OrderResponse findById(Long id);
    OrderResponse create(CreateOrderRequest request);
}
