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
        BigDecimal amount = new BigDecimal(body.get("amount").toString());
        int quantity = Integer.parseInt(String.valueOf(body.getOrDefault("quantity", 1)));
        amount = discountCatalog.apply(amount, discountCode);
        amount = pricingService.priceFor(customerId, amount);
        BigDecimal unitPrice = amount.divide(BigDecimal.valueOf(quantity));

        String txnId = paymentService.chargeCustomer(customerId, amount);
        orderRepository.save(customerId, amount, txnId);

        Map<String, Object> order = new HashMap<>();
        order.put("customerId", customerId);
        order.put("amount", amount);
        order.put("quantity", quantity);
        order.put("unitPrice", unitPrice);
        order.put("transactionId", txnId);
        order.put("discountCode", discountCode);
        order.put("status", "confirmed");
        orders.add(order);
        return order;
    }
}
