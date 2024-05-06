package com.cafekiosk.cafekiosk.spring.api.controller.product;

import com.cafekiosk.cafekiosk.spring.api.ApiResponse;
import com.cafekiosk.cafekiosk.spring.api.controller.product.dto.request.ProductCreateRequest;
import com.cafekiosk.cafekiosk.spring.api.service.product.ProductService;
import com.cafekiosk.cafekiosk.spring.api.service.product.response.ProductResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class ProductController {

    private final ProductService productService;

    @PostMapping("/api/v1/products/new")
    public ApiResponse<ProductResponse> createProduct(@Valid @RequestBody ProductCreateRequest request) {
        return ApiResponse.ok(productService.createProduct(request));
    }

    @GetMapping("/api/v1/products/selling")
    public List<ProductResponse> getSellingProducts() {
        return productService.getSellingProducts();
    }
}
