package com.victim.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import java.math.BigDecimal;

@Repository
public class OrderRepository {

    private final JdbcTemplate jdbc;

    public OrderRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    public void save(String customerId, BigDecimal amount, String transactionId) {
        jdbc.update(
            "INSERT INTO orders (customer_id, amount, txn_id) VALUES (?, ?, ?)",
            customerId, amount, transactionId
        );
    }
}
