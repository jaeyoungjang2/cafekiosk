package com.cafekiosk.cafekiosk.spring.api.controller.order;

import com.cafekiosk.cafekiosk.spring.api.ApiResponse;
import com.cafekiosk.cafekiosk.spring.api.controller.order.request.OrderCreateRequest;
import com.cafekiosk.cafekiosk.spring.api.service.order.OrderService;
import com.cafekiosk.cafekiosk.spring.api.service.order.response.OrderResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@RestController
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/api/v1/orders/new")
    public ApiResponse<OrderResponse> createOrder(@Valid @RequestBody OrderCreateRequest request) {
        LocalDateTime registeredDate = LocalDateTime.now();
        return ApiResponse.ok(orderService.createOrder(request.toServiceRequest(), registeredDate));
    }
}
