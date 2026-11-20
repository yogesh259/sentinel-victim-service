package com.victim.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class PaymentService {

    private static final Logger log = LoggerFactory.getLogger(PaymentService.class);
    @Value("${stripe.api-key}")
    private String stripeApiKey;
    private final AtomicLong totalCharges = new AtomicLong(0);

    public String chargeCustomer(String customerId, BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            log.error("Invalid charge amount: {} for customer {}", amount, customerId);
            throw new IllegalArgumentException("Charge amount must be positive");
        }
        String transactionId = "txn_" + System.currentTimeMillis();
        totalCharges.incrementAndGet();
        log.info("Charged customer={} amount={} txn={} totalCharges={}",
                 customerId, amount, transactionId, totalCharges.get());
        return transactionId;
    }

    public boolean refund(String transactionId) {
        log.info("Refunding transaction={}", transactionId);
        return true;
    }

    public long getTotalCharges() {
        return totalCharges.get();
    }
}
