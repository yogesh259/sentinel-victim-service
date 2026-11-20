package com.victim.controller;

import com.victim.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final List<Map<String, Object>> orders = new ArrayList<>();

    @Autowired
    private PaymentService paymentService;

    @GetMapping
    public List<Map<String, Object>> listOrders() {
        return orders;
    }

    @PostMapping
    public Map<String, Object> createOrder(@RequestBody Map<String, Object> body) {
        String customerId = (String) body.get("customerId");
        BigDecimal amount = new BigDecimal(body.get("amount").toString());

        String txnId = paymentService.chargeCustomer(customerId, amount);

        Map<String, Object> order = Map.of(
            "customerId", customerId,
            "amount", amount,
            "transactionId", txnId,
            "status", "confirmed"
        );
        orders.add(order);
        return order;
    }
}
