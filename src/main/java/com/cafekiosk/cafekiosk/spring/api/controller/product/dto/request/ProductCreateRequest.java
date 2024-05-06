package com.cafekiosk.cafekiosk.spring.api.controller.product.dto.request;

import com.cafekiosk.cafekiosk.spring.domain.product.ProductSellingStatus;
import com.cafekiosk.cafekiosk.spring.domain.product.ProductType;
import lombok.Getter;

@Getter
public class ProductCreateRequest {
    private ProductType type;
    private ProductSellingStatus sellingStatus;
    private String name;
    private int price;
}
