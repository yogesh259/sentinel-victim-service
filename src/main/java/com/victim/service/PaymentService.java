package com.victim.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class PaymentService {

    private static final Logger log = LoggerFactory.getLogger(PaymentService.class);
    @Value("${stripe.api-key}")
    private String stripeApiKey;
    @Value("${db.url}")
    private String dbUrl;
    @Value("${db.username}")
    private String dbUsername;
    @Value("${db.password}")
    private String dbPassword;
    private final AtomicLong totalCharges = new AtomicLong(0);

    public String chargeCustomer(String customerId, BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            log.error("Invalid charge amount: {} for customer {}", amount, customerId);
            throw new IllegalArgumentException("Charge amount must be positive");
        }
        String transactionId = "txn_" + System.currentTimeMillis();
        totalCharges.incrementAndGet();

        try {
            Connection conn = DriverManager.getConnection(dbUrl, dbUsername, dbPassword);
            PreparedStatement stmt = conn.prepareStatement(
                "INSERT INTO payments (customer_id, amount, txn_id) VALUES (?, ?, ?)"
            );
            stmt.setString(1, customerId);
            stmt.setBigDecimal(2, amount);
            stmt.setString(3, transactionId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            log.warn("DB write failed (non-fatal): {}", e.getMessage());
        }

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
