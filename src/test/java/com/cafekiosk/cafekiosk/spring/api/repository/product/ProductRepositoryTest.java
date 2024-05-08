package com.cafekiosk.cafekiosk.spring.api.repository.product;

import com.cafekiosk.cafekiosk.spring.IntegrationTestSupport;
import com.cafekiosk.cafekiosk.spring.api.controller.product.ProductRepository;
import com.cafekiosk.cafekiosk.spring.domain.product.Product;
import com.cafekiosk.cafekiosk.spring.domain.product.ProductSellingStatus;
import com.cafekiosk.cafekiosk.spring.domain.product.ProductType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.cafekiosk.cafekiosk.spring.domain.product.ProductSellingStatus.*;
import static com.cafekiosk.cafekiosk.spring.domain.product.ProductType.HANDMADE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

// 스프링 서버를 띄워서 테스트
//@SpringBootTest
// 스프링 서버를 띄워서 테스트
// 스프링 부트 테스트 보다 가볍다.
// JPA와 관련된 bean들만 띄워서 테스트 가능하기 때문에 SpringBootTest 조금 더 가볍다
//@DataJpaTest
//@ActiveProfiles("test")
@Transactional
class ProductRepositoryTest extends IntegrationTestSupport {

    @Autowired
    private ProductRepository productRepository;

    @DisplayName("원하는 판매 상태를 가진 상품들을 조회한다")
    @Test
    void findAllBySellingStatusIn() {
        // given
        Product product1 = createProduct("001", HANDMADE, SELLING, "아메리카노", 4000);

        Product product2 = createProduct("002", HANDMADE, HOLD, "카페라떼", 4500);

        Product product3 = createProduct("003", HANDMADE, STOP_SELLING, "아메리카노", 7000);

        productRepository.saveAll(List.of(product1, product2, product3));

        // when
        List<Product> products = productRepository.findAllBySellingStatusIn(List.of(SELLING, HOLD));

        // then
        // 일반적으로 리스트 테스트할 떄 아래와 같이 한다.
        assertThat(products)
                .hasSize(2)
                .extracting("productNumber", "name", "sellingStatus")
                .containsExactlyInAnyOrder(
                        tuple("001", "아메리카노", SELLING),
                        tuple("002", "카페라떼", HOLD)
                );
    }

    @DisplayName("상품번호 리스트로 상품들을 조회한다.")
    @Test
    void findAllByProductNumberIn() {
        // given
        Product product1 = createProduct("001", HANDMADE, SELLING, "아메리카노", 4000);
        Product product2 = createProduct("002", HANDMADE, HOLD, "카페라떼", 4500);
        Product product3 = createProduct("003", HANDMADE, STOP_SELLING, "아메리카노", 7000);

        productRepository.saveAll(List.of(product1, product2, product3));

        // when
        List<Product> products = productRepository.findAllByProductNumberIn(List.of("001", "002"));

        // then
        // 일반적으로 리스트 테스트할 떄 아래와 같이 한다.
        assertThat(products)
                .hasSize(2)
                .extracting("productNumber", "name", "sellingStatus")
                .containsExactlyInAnyOrder(
                        tuple("001", "아메리카노", SELLING),
                        tuple("002", "카페라떼", HOLD)
                );
    }

    @DisplayName("가장 마지막으로 저장한 상품의 상품번호를 읽어온다.")
    @Test
    void findLatestProductNumberNumber() {
        // given
        String targetProductNumber = "003";
        Product product1 = createProduct("001", HANDMADE, SELLING, "아메리카노", 4000);
        Product product2 = createProduct("002", HANDMADE, HOLD, "카페라떼", 4500);
        Product product3 = createProduct(targetProductNumber, HANDMADE, STOP_SELLING, "아메리카노", 7000);

        productRepository.saveAll(List.of(product1, product2, product3));

        // when
        String latestProductNumber = productRepository.findLatestProductNumber();

        // then
        assertThat(latestProductNumber).isEqualTo(targetProductNumber);
    }

    @DisplayName("가장 마지막으로 저장한 상품의 상품번호를 읽어올 때, 상품이 하나도 없는 경우에는 null을 반환한다.")
    @Test
    void findLatestProductNumberNumberWhenProductIsEmpty() {
        // when
        String latestProductNumber = productRepository.findLatestProductNumber();

        // then
        assertThat(latestProductNumber).isNull();
    }

    private Product createProduct(String productNumber, ProductType type, ProductSellingStatus sellingStatus, String name, int price) {
        return Product.builder()
                .productNumber(productNumber)
                .type(type)
                .sellingStatus(sellingStatus)
                .name(name)
                .price(price)
                .build();
    }
}