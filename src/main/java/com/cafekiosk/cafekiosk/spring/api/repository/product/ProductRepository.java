package com.cafekiosk.cafekiosk.spring.api.repository.product;

import com.cafekiosk.cafekiosk.spring.domain.product.Product;
import com.cafekiosk.cafekiosk.spring.domain.product.ProductSellingStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    /**
     * select *
     * from product
     * where selling_status in ('SELLING', "HOLD');
     */
    List<Product> findAllBySellingStatusIn(List<ProductSellingStatus> sellingStatuses);
}