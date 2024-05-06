package com.cafekiosk.cafekiosk.spring.api.controller.product.dto.request;

import com.cafekiosk.cafekiosk.spring.domain.product.Product;
import com.cafekiosk.cafekiosk.spring.domain.product.ProductSellingStatus;
import com.cafekiosk.cafekiosk.spring.domain.product.ProductType;
import lombok.Builder;
import lombok.Getter;

@Getter
public class ProductCreateRequest {
    private ProductType type;
    private ProductSellingStatus sellingStatus;
    private String name;
    private int price;

    @Builder
    public ProductCreateRequest(ProductType type, ProductSellingStatus sellingStatus, String name, int price) {
        this.type = type;
        this.sellingStatus = sellingStatus;
        this.name = name;
        this.price = price;
    }

    public Product toEntity(String nextProductNumber) {
        return Product.builder()
                .productNumber(nextProductNumber)
                .type(type)
                .sellingStatus(sellingStatus)
                .name(name)
                .price(price)
                .build();
    }
}
