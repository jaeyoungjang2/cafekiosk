package com.cafekiosk.cafekiosk.spring.api.service.order;

import com.cafekiosk.cafekiosk.spring.api.controller.order.request.OrderCreateRequest;
import com.cafekiosk.cafekiosk.spring.api.controller.product.ProductRepository;
import com.cafekiosk.cafekiosk.spring.api.service.order.request.OrderCreateServiceRequest;
import com.cafekiosk.cafekiosk.spring.api.service.order.response.OrderResponse;
import com.cafekiosk.cafekiosk.spring.domain.order.Order;
import com.cafekiosk.cafekiosk.spring.domain.order.OrderRepository;
import com.cafekiosk.cafekiosk.spring.domain.product.Product;
import com.cafekiosk.cafekiosk.spring.domain.product.ProductType;
import com.cafekiosk.cafekiosk.spring.domain.stock.Stock;
import com.cafekiosk.cafekiosk.spring.domain.stock.StockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Transactional
@Service
@RequiredArgsConstructor
public class OrderService {
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;
    private final StockRepository stockRepository;

    /**
     *
     * 재고 감소 -> 동시성 고민
     * optimistic lock / pessimistic lock / ...
     * 데이터에 대한 lock을 잡고 순차적으로 처리할 수 있도록 함
     *
     */
    public OrderResponse createOrder(OrderCreateServiceRequest request, LocalDateTime registeredDateTime) {
        List<String> productNumbers = request.getProductNumbers();
        List<Product> products = findProductsBy(productNumbers);

        Order order = Order.create(products, registeredDateTime);

        deductStockQuantities(products, productNumbers);

        // 재영 코드
//        List<Stock> stocks = stockRepository.findAllByProductNumberIn(productNumbers);
//        for (Product product : products) {
//            for (Stock stock : stocks) {
//                if (product.getProductNumber().equals(stock.getProductNumber()) && List.of(ProductType.BOTTLE, ProductType.BAKERY).contains(product.getType())) {
//                    stock.ordered();
//                }
//            }
//        }

        Order savedOrder = orderRepository.save(order);
        return OrderResponse.of(savedOrder);
    }

    private void deductStockQuantities(List<Product> products, List<String> productNumbers) {
        // 재고 차감 체크가 필요한 상품들 filter
        List<String> stockProductNumbers = extractStockProductNumbers(products);

        // 재고 엔티티 조회
        Map<String, Stock> stockMap = createStockMapBy(productNumbers);

        // 상품별 counting
        Map<String, Long> productCountingMap = createCountingMapBy(stockProductNumbers);

        // 재고 차감 시도
        for (String stockProductNumber : stockMap.keySet()) {
            Stock stock = stockMap.get(stockProductNumber);
            int quantity = productCountingMap.get(stockProductNumber).intValue();

            if (stock.isQuantityLessThan(quantity)) {
                throw new IllegalArgumentException("재고가 부족한 상품이 있습니다.");
            }
            stock.deductQuantity(quantity);
        }
    }

    private static Map<String, Long> createCountingMapBy(List<String> stockProductNumbers) {
        Map<String, Long> productCountingMap = stockProductNumbers.stream()
                .collect(Collectors.groupingBy(p -> p, Collectors.counting()));
        return productCountingMap;
    }

    private Map<String, Stock> createStockMapBy(List<String> productNumbers) {
        List<Stock> stocks = stockRepository.findAllByProductNumberIn(productNumbers);
        Map<String, Stock> stockMap = stocks.stream()
                .collect(Collectors.toMap(Stock::getProductNumber, s -> s));
        return stockMap;
    }

    private static List<String> extractStockProductNumbers(List<Product> products) {
        List<String> stockProductNumbers = products.stream()
                .filter(product -> ProductType.containsStockType(product.getType()))
                .map(Product::getProductNumber)
                .collect(Collectors.toList());
        return stockProductNumbers;
    }

    // productNumbers의 수 만큼 products가 나오도록 함
    private List<Product> findProductsBy(List<String> productNumbers) {
        List<Product> products = productRepository.findAllByProductNumberIn(productNumbers);
        Map<String, Product> productMap = products.stream()
                .collect(Collectors.toMap(Product::getProductNumber, p -> p));

        return productNumbers.stream()
                .map(productMap::get)
                .collect(Collectors.toList());
    }
}
