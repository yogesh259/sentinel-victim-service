package com.victim.service;

import com.victim.repository.OrderRepository;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class OrderService {

    private final List<Map<String, Object>> orders = new ArrayList<>();
    private final PaymentService paymentService;
    private final DiscountCatalog discountCatalog;
    private final PricingService pricingService;
    private final OrderRepository orderRepository;

    public OrderService(PaymentService paymentService, DiscountCatalog discountCatalog,
                        PricingService pricingService, OrderRepository orderRepository) {
        this.paymentService = paymentService;
        this.discountCatalog = discountCatalog;
        this.pricingService = pricingService;
        this.orderRepository = orderRepository;
    }

    public List<Map<String, Object>> listOrders() {
        return orders;
    }

    public Map<String, Object> createOrder(Map<String, Object> body) {
        String customerId = String.valueOf(body.get("customerId"));
        String discountCode = (String) body.get("discountCode");
        BigDecimal total = new BigDecimal(body.get("amount").toString());
        int quantity = Integer.parseInt(String.valueOf(body.getOrDefault("quantity", 1)));
        total = discountCatalog.apply(total, discountCode);
        total = pricingService.priceFor(customerId, total);
        BigDecimal unitPrice = total.divide(BigDecimal.valueOf(quantity));

        String transactionId = paymentService.chargeCustomer(customerId, total);
        orderRepository.save(customerId, total, transactionId);

        Map<String, Object> order = new HashMap<>();
        order.put("customerId", customerId);
        order.put("amount", total);
        order.put("quantity", quantity);
        order.put("unitPrice", unitPrice);
        order.put("transactionId", transactionId);
        order.put("discountCode", discountCode);
        order.put("status", "confirmed");
        orders.add(order);
        return order;
    }
}
