package com.seveneleven.backend.controller;

import com.seveneleven.backend.dto.CreateOrderRequest;
import com.seveneleven.backend.dto.OrderResponse;
import com.seveneleven.backend.service.IOrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final IOrderService orderService;

    @GetMapping
    public List<OrderResponse> getAll() {
        return orderService.findAll();
    }

    @GetMapping("/{id}")
    public OrderResponse getById(@PathVariable Long id) {
        return orderService.findById(id);
    }

    @PostMapping
    public OrderResponse create(@RequestBody @Valid CreateOrderRequest request) {
        return orderService.create(request);
    }
}
