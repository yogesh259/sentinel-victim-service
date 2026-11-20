package com.victim.service;

import org.springframework.stereotype.Service;
import java.math.BigDecimal;

@Service
public class PaymentService {

    @Value("${stripe.api-key}")
    private String stripeApiKey;

    public String chargeCustomer(String customerId, BigDecimal amount) {
        // Simulate Stripe API call
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Charge amount must be positive");
        }
        String transactionId = "txn_" + System.currentTimeMillis();
        System.out.println("Charged " + customerId + " $" + amount + " -> " + transactionId);
        return transactionId;
    }

    public boolean refund(String transactionId) {
        System.out.println("Refunded " + transactionId);
        return true;
    }
}
