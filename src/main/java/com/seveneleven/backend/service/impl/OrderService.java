package com.seveneleven.backend.service.impl;

import com.seveneleven.backend.dto.CreateOrderRequest;
import com.seveneleven.backend.dto.OrderItemRequest;
import com.seveneleven.backend.dto.OrderItemResponse;
import com.seveneleven.backend.dto.OrderResponse;
import com.seveneleven.backend.entity.Order;
import com.seveneleven.backend.entity.OrderItem;
import com.seveneleven.backend.entity.Product;
import com.seveneleven.backend.exception.BadRequestException;
import com.seveneleven.backend.repository.OrderRepository;
import com.seveneleven.backend.repository.ProductRepository;
import com.seveneleven.backend.service.IOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService implements IOrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;

    @Override
    public List<OrderResponse> findAll() {
        return orderRepository.findAll().stream().map(this::toResponse).toList();
    }

    @Override
    public OrderResponse findById(Long id) {
        return toResponse(orderRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("Order not found with id " + id)));
    }

    @Override
    @Transactional
    public OrderResponse create(CreateOrderRequest request) {
        Order order = new Order();
        order.setCustomerName(request.customerName());
        order.setPhone(request.phone());
        order.setAddress(request.address());

        BigDecimal total = BigDecimal.ZERO;

        for (OrderItemRequest itemReq : request.items()) {
            Product product = productRepository.findById(itemReq.productId())
                    .orElseThrow(() -> new BadRequestException("Product not found with id " + itemReq.productId()));

            int quantity = itemReq.quantity() == null ? 0 : itemReq.quantity();
            if (quantity <= 0) {
                throw new BadRequestException("Quantity must be greater than 0");
            }
            if (product.getStock() == null || product.getStock() < quantity) {
                throw new BadRequestException("Not enough stock for product " + product.getName());
            }

            product.setStock(product.getStock() - quantity);

            OrderItem item = new OrderItem();
            item.setProductId(product.getId());
            item.setProductName(product.getName());
            item.setUnitPrice(product.getPrice());
            item.setQuantity(quantity);
            item.setLineTotal(product.getPrice().multiply(BigDecimal.valueOf(quantity)));

            total = total.add(item.getLineTotal());
            order.addItem(item);
        }

        order.setTotalAmount(total);
        orderRepository.save(order);
        return toResponse(order);
    }

    private OrderResponse toResponse(Order order) {
        List<OrderItemResponse> items = order.getItems().stream()
                .map(i -> new OrderItemResponse(
                        i.getId(),
                        i.getProductId(),
                        i.getProductName(),
                        i.getUnitPrice(),
                        i.getQuantity(),
                        i.getLineTotal()
                ))
                .toList();

        return new OrderResponse(
                order.getId(),
                order.getCustomerName(),
                order.getPhone(),
                order.getAddress(),
                order.getTotalAmount(),
                order.getStatus(),
                order.getCreatedAt(),
                order.getUpdatedAt(),
                items
        );
    }
}
