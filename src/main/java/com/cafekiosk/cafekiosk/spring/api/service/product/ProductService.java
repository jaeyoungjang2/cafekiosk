package com.cafekiosk.cafekiosk.spring.api.service.product;

import com.cafekiosk.cafekiosk.spring.api.controller.product.request.ProductCreateRequest;
import com.cafekiosk.cafekiosk.spring.api.service.product.request.ProductCreateServiceRequest;
import com.cafekiosk.cafekiosk.spring.api.service.product.response.ProductResponse;
import com.cafekiosk.cafekiosk.spring.domain.product.Product;
import com.cafekiosk.cafekiosk.spring.api.controller.product.ProductRepository;
import com.cafekiosk.cafekiosk.spring.domain.product.ProductSellingStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductNumberFactory productNumberFactory;

    // 동시성 이슈 (상품을 동시에 등록하는 경우)
    // 증가하는 형태의 번호를 등록하는 경우 여러명이 동시에 등록하는 경우
    // productNumber field에다가 db에 unique index 제약 조건을 걸고 시도했는데 실패하면 시스템에서 알아서 3회 이상 재시도를 하도록
    // 동시 접속자가 너무 많은 경우에는 아예 productNumber 자체를 증가하는 값이 아니라 정책을 변경한다, UUID와 같은

    @Transactional
    public ProductResponse createProduct(ProductCreateServiceRequest request) {
        String nextProductNumber = productNumberFactory.createNextProductNumber();

        Product product = request.toEntity(nextProductNumber);
        Product savedProduct = productRepository.save(product);

        return ProductResponse.of(savedProduct);
    }

    public List<ProductResponse> getSellingProducts() {
        List<Product> products = productRepository.findAllBySellingStatusIn(ProductSellingStatus.forDisplay());

        return products.stream()
                .map(ProductResponse::of)
                .collect(Collectors.toList());
    }
}
