package com.cafekiosk.cafekiosk.spring.api.service.order;

import com.cafekiosk.cafekiosk.spring.domain.orderproduct.OrderProduct;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderProductRepository extends JpaRepository<OrderProduct, Long> {
}
