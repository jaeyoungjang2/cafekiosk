package com.cafekiosk.cafekiosk.spring.api.service.product;

import com.cafekiosk.cafekiosk.spring.api.controller.product.dto.request.ProductCreateRequest;
import com.cafekiosk.cafekiosk.spring.api.service.product.response.ProductResponse;
import com.cafekiosk.cafekiosk.spring.domain.product.Product;
import com.cafekiosk.cafekiosk.spring.api.repository.product.ProductRepository;
import com.cafekiosk.cafekiosk.spring.domain.product.ProductSellingStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ProductService {

    private final ProductRepository productRepository;

    public void createProduct(ProductCreateRequest request) {
        // productNumber 상품의 고유 번호
        // DB에서 마지막 저장된 Product의 상품 번호를 읽어와서 +1
        // 009 -> 010
        String latestProductNumber = productRepository.findLatestProductNumber();

    }


    public List<ProductResponse> getSellingProducts() {
        // 판매중 또는 판매 보류인 상품만
        List<Product> products = productRepository.findAllBySellingStatusIn(ProductSellingStatus.forDisplay());

        return products.stream()
                .map(ProductResponse::of)
                .collect(Collectors.toList());
    }
}
