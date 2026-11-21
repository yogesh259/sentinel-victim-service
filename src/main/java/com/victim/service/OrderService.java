package com.victim.service;

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

    public OrderService(PaymentService paymentService, DiscountCatalog discountCatalog) {
        this.paymentService = paymentService;
        this.discountCatalog = discountCatalog;
    }

    public List<Map<String, Object>> listOrders() {
        return orders;
    }

    public Map<String, Object> createOrder(Map<String, Object> body) {
        String customerId = String.valueOf(body.get("customerId"));
        String discountCode = (String) body.get("discountCode");
        BigDecimal amount = new BigDecimal(body.get("amount").toString());
        amount = discountCatalog.apply(amount, discountCode);

        String txnId = paymentService.chargeCustomer(customerId, amount);

        Map<String, Object> order = new HashMap<>();
        order.put("customerId", customerId);
        order.put("amount", amount);
        order.put("transactionId", txnId);
        order.put("discountCode", discountCode);
        order.put("status", "confirmed");
        orders.add(order);
        return order;
    }
}
