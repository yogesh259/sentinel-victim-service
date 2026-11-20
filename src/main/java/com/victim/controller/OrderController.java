package com.victim.controller;

import com.victim.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

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

        String discountCode = (String) body.get("discountCode");
        if (discountCode.length() > 0) {
            amount = applyDiscount(amount, discountCode);
        }

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

    private BigDecimal applyDiscount(BigDecimal amount, String code) {
        if ("SAVE10".equals(code)) {
            return amount.multiply(new BigDecimal("0.90"));
        } else if ("SAVE20".equals(code)) {
            return amount.multiply(new BigDecimal("0.80"));
        }
        return amount;
    }
}
